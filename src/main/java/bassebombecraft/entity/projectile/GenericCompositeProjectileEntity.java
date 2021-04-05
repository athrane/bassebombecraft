package bassebombecraft.entity.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.PROJECTILE_TRAIL;
import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.genericProjectileEntityProjectileDuration;
import static bassebombecraft.config.ModConfiguration.genericProjectileEntityProjectileHomingAoeRange;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.util.function.Predicates.hasDifferentIds;
import static bassebombecraft.util.function.Predicates.isntProjectileThrower;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static bassebombecraft.world.WorldUtils.isLogicalServer;
import static net.minecraft.entity.projectile.ProjectileHelper.rayTrace;
import static net.minecraftforge.event.ForgeEventFactory.onProjectileImpact;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import bassebombecraft.config.ProjectileEntityConfig;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.entity.Electrocute2;
import bassebombecraft.operator.entity.FindEntities2;
import bassebombecraft.operator.projectile.path.AccelerateProjectilePath;
import bassebombecraft.operator.projectile.path.CircleProjectilePath;
import bassebombecraft.operator.projectile.path.DeaccelerateProjectilePath;
import bassebombecraft.operator.projectile.path.DecreaseGravityProjectilePath;
import bassebombecraft.operator.projectile.path.HomingProjectilePath;
import bassebombecraft.operator.projectile.path.IncreaseGravityProjectilePath;
import bassebombecraft.operator.projectile.path.RandomProjectilePath;
import bassebombecraft.operator.projectile.path.SineProjectilePath;
import bassebombecraft.operator.projectile.path.TeleportProjectilePath;
import bassebombecraft.operator.projectile.path.ZigZagProjectilePath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Generic projectile implementation to support composite items.
 * 
 * This is a super class for actual projectile implementations.
 */
public class GenericCompositeProjectileEntity extends ProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = GenericCompositeProjectileEntity.class.getSimpleName();

	/**
	 * Client side effect rendering operator.
	 */
	static final Operator2 RENDERING_TRAIL_OP = new AddGraphicalEffectAtClient2(PROJECTILE_TRAIL);

	/**
	 * Random projectile path operator.
	 */
	static final Operator2 RANDOM_PATH_OPERATOR = new RandomProjectilePath();

	/**
	 * Accelerate projectile path operator.
	 */
	static final Operator2 ACCELERATE_PATH_OPERATOR = new AccelerateProjectilePath();

	/**
	 * De-accelerate projectile path operator.
	 */
	static final Operator2 DEACCELERATE_PATH_OPERATOR = new DeaccelerateProjectilePath();

	/**
	 * Zig Zag projectile path operator.
	 */
	static final Operator2 ZIGZAG_PATH_OPERATOR = new ZigZagProjectilePath();

	/**
	 * Sine projectile path operator.
	 */
	static final Operator2 SINE_PATH_OPERATOR = new SineProjectilePath();

	/**
	 * Spiral projectile path operator.
	 */
	static final Operator2 SPIRAL_PATH_OPERATOR = new CircleProjectilePath();

	/**
	 * Increase gravity projectile path operator.
	 */
	static final Operator2 INCREASE_GRAVITY_PATH_OPERATOR = new IncreaseGravityProjectilePath();

	/**
	 * Decrease gravity projectile path operator.
	 */
	static final Operator2 DECREASE_GRAVITY_PATH_OPERATOR = new DecreaseGravityProjectilePath();

	/**
	 * Teleport projectile path operator.
	 */
	static final Operator2 TELEPORT_PATH_OPERATOR = new TeleportProjectilePath();

	/**
	 * Homing projectile path operator.
	 */
	static Optional<Operator2> optHomingPathOperator = Optional.empty();

	/**
	 * Create homing path operator.
	 */
	static Supplier<Operator2> splHomingPathOp = () -> {

		Function<Ports, Entity> fnGetSource = DefaultPorts.getFnGetEntity1();

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(fnGetSource, p).getEntityWorld();

		// FindEntities2: get function to create exclusion predicate using the source
		// entity
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(fnGetSource, p))
				.and(isntProjectileThrower(applyV(fnGetSource, p)));

		// FindEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> genericProjectileEntityProjectileHomingAoeRange.get().intValue();

		return new Sequence2(
				new FindEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange, getBcSetEntities1()),
				new HomingProjectilePath());
	};

	/**
	 * Homing projectile path operator.
	 */
	static final Operator2 HOMING_PATH_OPERATOR = splHomingPathOp.get();

	/**
	 * Electrocute operator.
	 */
	static final Operator2 ELECTROCUTE_OPERATOR = new Electrocute2();

	/**
	 * Projectile duration.
	 */
	int duration;

	/**
	 * Invoker UUID.
	 */
	UUID invokerUUID;

	/**
	 * Projectile invoker
	 */
	LivingEntity invoker;

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires the
	 * projectile. add by this class.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the projectile will be
	 * removed from the world..
	 */
	Consumer<String> cRemovalCallback = id -> remove();

	/**
	 * Projectile modifier ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports projectileModifierPorts = getInstance();

	/**
	 * Projectile rendering ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports renderingPorts = getInstance();

	/**
	 * Projectile entity configuration.
	 */
	ProjectileEntityConfig projectileConfig;

	/**
	 * Client side rendering operator.
	 */
	Operator2 renderingOp;

	/**
	 * Constructor
	 * 
	 * @param type   entity type.
	 * @param world  world object.
	 * @param config projectile entity configuration.
	 */
	public GenericCompositeProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, World world, ProjectileEntityConfig config) {		
		super(type, world);
		projectileConfig = config;
		ParticleRenderingInfo info = createInfoFromConfig(projectileConfig.particles);
		renderingOp = new AddParticlesFromPosAtClient2(info);
		duration = genericProjectileEntityProjectileDuration.get();
		initialiseDuration();
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param shooter projectile shooter.
	 * @param world   world object.
	 * @param config  projectile entity configuration.
	 */
	public GenericCompositeProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, LivingEntity shooter, ProjectileEntityConfig config) {
		this(type, shooter.getEntityWorld(), config);
		this.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1, shooter.getPosZ());
		this.setShooter(shooter);
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

		// calculate motion
		Vector3d motionVector = (new Vector3d(x, y, z)).normalize()
				.add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
				.scale((double) velocity);
		this.setMotion(motionVector);

		// calculate yaw and pitch for projectile
		float f = MathHelper.sqrt(horizontalMag(motionVector));
		this.rotationYaw = (float) (MathHelper.atan2(motionVector.x, motionVector.z)
				* (double) (180F / (float) Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(motionVector.y, f) * (double) (180F / (float) Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;

	}

	/**
	 * Helper function for shooting the projectile using the configuration
	 * properties of the projectile.
	 * 
	 * @param orientation orientation vector for direction of projectile.
	 */
	public void doShoot(Vector3d orientation) {
		setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		double force = projectileConfig.force.get();
		double inaccuracy = projectileConfig.inaccuracy.get();
		double velocity = force * orientation.length();
		shoot(orientation.getX(), orientation.getY(), orientation.getZ(), (float) velocity, (float) inaccuracy);

		// add trail effect at clients
		addTrailGraphicalEffect();
	}

	/**
	 * Initialise duration for the projectile.
	 */
	void initialiseDuration() {

		// get repository
		DurationRepository repository = null;
		if (isLogicalServer(world))
			repository = getProxy().getServerDurationRepository();
		else
			repository = getProxy().getClientDurationRepository();

		// add duration
		repository.add(getUniqueID().toString(), duration, cRemovalCallback);
	}

	/**
	 * Updates the entity motion client side, called by packets from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.setMotion(x, y, z);
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (double) (180F / (float) Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	@Override
	public void tick() {
		super.tick();

		try {

			// exit if on client side
			if (!isLogicalClient(getEntityWorld())) {

				// calculate collision with block or entity
				RayTraceResult result = calculateCollision();

				// if hit then process collision
				if (result.getType() != RayTraceResult.Type.MISS)
					onImpact(result);

				// process projectile modifiers
				processCompositeModifiers();

				// send particle rendering info to client
				addRendering();

				// update ports counter
				projectileModifierPorts.incrementCounter();

			}

			// Update motion and position
			updateMotionAndPosition();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Process project collision.
	 * 
	 * @param result ray trace result with collision information.
	 */
	void onImpact(RayTraceResult result) {

		// exit if on client side
		if (isLogicalClient(getEntityWorld()))
			return;

		// post forge impact event
		boolean isCancelled = onProjectileImpact(this, result);

		// exit if event was cancelled
		if (isCancelled)
			return;

		// process hit entity
		if (result.getType() == RayTraceResult.Type.ENTITY) {

			// get hit entity
			Entity target = ((EntityRayTraceResult) result).getEntity();

			// add damage
			addEntityDamage(target);
		}

		// process hit block
		if (result.getType() == RayTraceResult.Type.BLOCK) {

			// get hit block
			BlockPos pos = ((BlockRayTraceResult) result).getPos();
		}

		// Digging is handled by ProjectileModifierEventHandler. Which cancels the
		// projectile impact event and exits this method prior to this point.
		this.remove();
	}

	/**
	 * Add damage to hit entity.
	 * 
	 * @param target hit target entity.
	 */
	void addEntityDamage(Entity target) {

		// TODO: should invoker be immune to projectile hits?
		if (target.getUniqueID().equals(this.invokerUUID))
			return;

		double amount = projectileConfig.damage.get();
		DamageSource source = DamageSource.causeIndirectMagicDamage(this, invoker);
		target.attackEntityFrom(source, (float) amount);
	}

	/**
	 * Calculate collision.
	 * 
	 * Implementation inspired by the tick() method in {@linkplain ThrowableEntity}.
	 * 
	 * @return ray trace result with block or entity collision.
	 */
	RayTraceResult calculateCollision() {

		// get AABB for collision
		AxisAlignedBB aabb = this.getBoundingBox().expand(this.getMotion()).grow(1);

		// define filter
		Predicate<Entity> filter = entity -> !entity.isSpectator() && entity.canBeCollidedWith()
				&& entity != this.invoker;

		// ray trace for collision
		return rayTrace(this, aabb, filter, RayTraceContext.BlockMode.COLLIDER, true);
	}

	/**
	 * Process defined composite projectile modifiers.
	 */
	void processCompositeModifiers() {

		// get tags
		Set<String> tags = this.getTags();

		// exit if no tags is defined
		if (tags.isEmpty())
			return;

		// handle: random path
		if (tags.contains(RandomProjectilePath.NAME))
			calculateRandomPath();

		// handle: accelerate
		if (tags.contains(AccelerateProjectilePath.NAME))
			calculateAccelerationPath();

		// handle: deaccelerate
		if (tags.contains(DeaccelerateProjectilePath.NAME))
			calculateDeaccelerationPath();

		// handle: zig zag
		if (tags.contains(ZigZagProjectilePath.NAME))
			calculateZigZagPath();

		// handle: sine
		if (tags.contains(SineProjectilePath.NAME))
			calculateSinePath();

		// handle: spiral
		if (tags.contains(CircleProjectilePath.NAME))
			calculateSpiralPath();

		// handle: increase gravity
		if (tags.contains(IncreaseGravityProjectilePath.NAME))
			calculateIncreaseGravityPath();

		// handle: decrease gravity
		if (tags.contains(DecreaseGravityProjectilePath.NAME))
			calculateDecreaseGravityPath();

		// handle: teleport path
		if (tags.contains(TeleportProjectilePath.NAME))
			calculateTeleportPath();

		// handle: homing
		if (tags.contains(HomingProjectilePath.NAME))
			calculateHomingPath();

		// handle: electrocute
		if (tags.contains(Electrocute2.NAME))
			electrocute();
	}

	/**
	 * Execute random path modifier operator.
	 */
	void calculateRandomPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, RANDOM_PATH_OPERATOR);
	}

	/**
	 * Execute acceleration path modifier operator.
	 */
	void calculateAccelerationPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, ACCELERATE_PATH_OPERATOR);
	}

	/**
	 * Execute deacceleration path modifier operator.
	 */
	void calculateDeaccelerationPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, DEACCELERATE_PATH_OPERATOR);
	}

	/**
	 * Execute zig zag path modifier operator.
	 */
	void calculateZigZagPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, ZIGZAG_PATH_OPERATOR);
	}

	/**
	 * Execute sine path modifier operator.
	 */
	void calculateSinePath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, SINE_PATH_OPERATOR);
	}

	/**
	 * Execute spiral path modifier operator.
	 */
	void calculateSpiralPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, SPIRAL_PATH_OPERATOR);
	}

	/**
	 * Execute increase gravity path modifier operator.
	 */
	void calculateIncreaseGravityPath() {
		projectileModifierPorts.setDouble1(projectileConfig.gravity.get());
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, INCREASE_GRAVITY_PATH_OPERATOR);
	}

	/**
	 * Execute decrease gravity path modifier operator.
	 */
	void calculateDecreaseGravityPath() {
		projectileModifierPorts.setDouble1(projectileConfig.gravity.get());
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, DECREASE_GRAVITY_PATH_OPERATOR);
	}

	/**
	 * Execute teleport path modifier operator.
	 */
	void calculateTeleportPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, TELEPORT_PATH_OPERATOR);
	}

	/**
	 * Execute homing path modifier operator.
	 */
	void calculateHomingPath() {
		projectileModifierPorts.setEntity1(this);
		run(projectileModifierPorts, HOMING_PATH_OPERATOR);
	}

	/**
	 * Execute electrocute operator.
	 */
	void electrocute() {
		projectileModifierPorts.setEntity1(this);
		projectileModifierPorts.setEntity2(getThrower());
		run(projectileModifierPorts, ELECTROCUTE_OPERATOR);
	}

	/**
	 * Update motion and position of the projectile.
	 */
	void updateMotionAndPosition() {
		Vector3d motionVec = this.getMotion();
		Vector3d positionVec = this.getPositionVec();

		// calculate motion drag
		// TODO: Add as property
		float motionScale = this.isInWater() ? getWaterDrag() : getAirDrag();

		// calculate motion and position
		Vector3d nextMotionVec = motionVec.scale(motionScale);
		Vector3d nextPositionVec = motionVec.add(positionVec);
		this.setMotion(nextMotionVec.getX(), nextMotionVec.getY() - getGravity(), nextMotionVec.getZ());
		this.setPosition(nextPositionVec.getX(), nextPositionVec.getY(), nextPositionVec.getZ());
	}

	@Override
	protected void registerData() {
		// NO-OP
		// TODO: investigate this method, see implementation in ProjectileItemEntity
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.invokerUUID = compound.getUniqueId("Invoker");
		if (!this.world.isRemote() && this.invokerUUID != null) {
			((ServerWorld) this.world).getEntityByUuid(this.invokerUUID);
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		if (this.invokerUUID != null)
			compound.putUniqueId("Invoker", invokerUUID);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public LivingEntity getThrower() {
		return this.invoker;
	}

	float getWaterDrag() {
		return 0.6f;
	}

	float getAirDrag() {
		return 0.99f;
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	double getGravity() {
		return projectileConfig.gravity.get();
	}

	/**
	 * Returns the projectile configuration.
	 * 
	 * @return the projectile configuration
	 */
	public ProjectileEntityConfig getConfiguration() {
		return projectileConfig;
	}

	/**
	 * Add particle rendering on update tick.
	 */
	void addRendering() {
		renderingPorts.setBlockPosition1(getPosition());
		run(renderingPorts, renderingOp);
	}

	/**
	 * Send graphical effect to client
	 */
	void addTrailGraphicalEffect() {
		renderingPorts.setDouble1((double) duration);
		renderingPorts.setEntity1(this);
		renderingPorts.setEntity2(getThrower());
		run(renderingPorts, RENDERING_TRAIL_OP);
	}

}
