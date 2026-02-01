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
import static bassebombecraft.util.function.Predicates.isntProjectileShooter;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static bassebombecraft.world.WorldUtils.isLogicalServer;
import static net.minecraft.world.entity.projectile.ProjectileUtil.getHitResult;
import static net.minecraftforge.event.ForgeEventFactory.onProjectileImpact;

import java.util.Set;
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
import bassebombecraft.operator.entity.Contagion2;
import bassebombecraft.operator.entity.Electrocute2;
import bassebombecraft.operator.entity.FindEntities2;
import bassebombecraft.operator.entity.Wildfire2;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Generic projectile implementation to support composite items.
 * 
 * This is a super class for actual projectile implementations.
 */
public class GenericCompositeProjectileEntity extends Projectile {

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
	 * Create homing path operator.
	 */
	static Supplier<Operator2> splHomingPathOp = () -> {

		Function<Ports, Entity> fnGetSource = DefaultPorts.getFnGetEntity1();

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).blockPosition();

		// FindEntities2: get world from source entity
		Function<Ports, Level> fnGetWorld = p -> applyV(fnGetSource, p).getCommandSenderWorld();

		// FindEntities2: get function to create exclusion predicate using the source
		// entity
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(fnGetSource, p))
				.and(isntProjectileShooter(applyV(fnGetSource, p)));

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
	 * Wildfire operator.
	 */
	static final Operator2 WILDFIRE_OPERATOR = new Wildfire2();

	/**
	 * Contagion operator.
	 */
	static final Operator2 CONTAGION_OPERATOR = new Contagion2();
	
	/**
	 * Projectile duration.
	 */
	int duration;

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires the
	 * projectile. add by this class.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the projectile will be
	 * removed from the world..
	 */
	Consumer<String> cRemovalCallback = id -> remove(Entity.RemovalReason.DISCARDED);

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
	public GenericCompositeProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, Level world,
			ProjectileEntityConfig config) {
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
	public GenericCompositeProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type,
			LivingEntity shooter, ProjectileEntityConfig config) {
		this(type, shooter.getCommandSenderWorld(), config);
		this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
		this.setOwner(shooter);
	}

	/**
	 * Helper function for shooting the projectile using the configuration
	 * properties of the projectile.
	 * 
	 * Adds trail effect to projectile.
	 * 
	 * @param orientation orientation vector for direction of projectile.
	 */
	public void shootUsingProjectileConfig(Vec3 orientation) {

		// shoot
		Entity shooter = getOwner();
		setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
		double force = projectileConfig.force.get();
		double inaccuracy = projectileConfig.inaccuracy.get();
		double velocity = force * orientation.length();
		shoot(orientation.x(), orientation.y(), orientation.z(), (float) velocity, (float) inaccuracy);

		// add trail effect at clients
		addTrailGraphicalEffect();
	}

	/**
	 * Initialise duration for the projectile.
	 */
	void initialiseDuration() {

		// get repository
		DurationRepository repository = null;
		if (isLogicalServer(level))
			repository = getProxy().getServerDurationRepository();
		else
			repository = getProxy().getClientDurationRepository();

		// add duration
		repository.add(getUUID().toString(), duration, cRemovalCallback);
	}

	/**
	 * Updates the entity motion client side, called by packets from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public void lerpMotion(double x, double y, double z) {
		this.setDeltaMovement(x, y, z);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = Mth.sqrt((float) (x * x + z * z));
			float yRot = (float) (Mth.atan2(x, z) * (double) (180F / (float) Math.PI));
			float xRot = (float) (Mth.atan2(y, (double) f) * (double) (180F / (float) Math.PI));
			setYRot(yRot);
			setXRot(xRot);
			this.yRotO = yRot;
			this.xRotO = xRot;
		}
	}

	@Override
	public void tick() {
		super.tick();

		try {

			// exit if on client side
			if (!isLogicalClient(getCommandSenderWorld())) {

				// calculate collision with block or entity
				HitResult result = calculateCollision();

				// if hit then process collision
				if (result.getType() != HitResult.Type.MISS)
					handleImpact(result);

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
	void handleImpact(HitResult result) {

		// exit if on client side
		if (isLogicalClient(getCommandSenderWorld()))
			return;

		// post forge impact event
		boolean isCancelled = onProjectileImpact(this, result);

		// exit if event was cancelled
		if (isCancelled)
			return;

		// process hit entity
		if (result.getType() == HitResult.Type.ENTITY) {

			// get hit entity
			Entity target = ((EntityHitResult) result).getEntity();

			// add damage
			addEntityDamage(target);
		}

		// process hit block
		if (result.getType() == HitResult.Type.BLOCK) {

			// get hit block
			BlockPos pos = ((BlockHitResult) result).getBlockPos();
		}

		// Digging is handled by ProjectileModifierEventHandler. Which cancels the
		// projectile impact event and exits this method prior to this point.
		this.remove(Entity.RemovalReason.DISCARDED);
	}

	/**
	 * Add damage to hit entity.
	 * 
	 * @param target hit target entity.
	 */
	void addEntityDamage(Entity target) {

		// TODO: should invoker be immune to projectile hits?
		if (target.getUUID().equals(this.getUUID()))
			return;

		double amount = projectileConfig.damage.get();
		DamageSource source = DamageSource.indirectMagic(this, getOwner());
		target.hurt(source, (float) amount);
	}

	/**
	 * Calculate collision.
	 * 
	 * Implementation inspired by the tick() method in {@linkplain ThrowableEntity}.
	 * 
	 * @return ray trace result with block or entity collision.
	 */
	HitResult calculateCollision() {

		// define filter
		Predicate<Entity> filter = entity -> !entity.isSpectator() && entity.isPickable()
				&& entity != getOwner();

		// ray trace for collision
		return getHitResult(this, filter);
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
		
		// handle: wildfire
		if (tags.contains(Wildfire2.NAME))
			wildfire();		

		// handle: contagion
		if (tags.contains(Contagion2.NAME))
			contagion();				
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
		projectileModifierPorts.setEntity2(getOwner());
		run(projectileModifierPorts, ELECTROCUTE_OPERATOR);
	}

	/**
	 * Execute wildfire operator.
	 */
	void wildfire() {
		projectileModifierPorts.setEntity1(this);
		projectileModifierPorts.setEntity2(getOwner());
		run(projectileModifierPorts, WILDFIRE_OPERATOR);
	}

	/**
	 * Execute contagion operator.
	 */
	void contagion() {
		projectileModifierPorts.setEntity1(this);
		projectileModifierPorts.setEntity2(getOwner());
		run(projectileModifierPorts, CONTAGION_OPERATOR);
	}
	
	/**
	 * Update motion and position of the projectile.
	 */
	void updateMotionAndPosition() {
		Vec3 motionVec = this.getDeltaMovement();
		Vec3 positionVec = this.position();

		// calculate motion drag
		// TODO: Add as property
		float motionScale = this.isInWater() ? getWaterDrag() : getAirDrag();

		// calculate motion and position
		Vec3 nextMotionVec = motionVec.scale(motionScale);
		Vec3 nextPositionVec = motionVec.add(positionVec);
		this.setDeltaMovement(nextMotionVec.x(), nextMotionVec.y() - getGravity(), nextMotionVec.z());
		this.setPos(nextPositionVec.x(), nextPositionVec.y(), nextPositionVec.z());
	}

	@Override
	protected void defineSynchedData() {
		// NO-OP
		// TODO: investigate this method, see implementation in ProjectileItemEntity
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
		renderingPorts.setBlockPosition1(blockPosition());
		run(renderingPorts, renderingOp);
	}

	/**
	 * Send graphical effect to client
	 */
	void addTrailGraphicalEffect() {
		renderingPorts.setDouble1((double) duration);
		renderingPorts.setEntity1(this);
		renderingPorts.setEntity2(getOwner());
		run(renderingPorts, RENDERING_TRAIL_OP);
	}

}
