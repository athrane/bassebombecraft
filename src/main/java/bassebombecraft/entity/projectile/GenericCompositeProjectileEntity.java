package bassebombecraft.entity.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.PARTICLE_SPAWN_FREQUENCY;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.genericProjectileEntityProjectileDuration;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static bassebombecraft.world.WorldUtils.isLogicalServer;
import static net.minecraft.entity.projectile.ProjectileHelper.rayTrace;
import static net.minecraftforge.event.ForgeEventFactory.onProjectileImpact;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import bassebombecraft.config.ProjectileEntityConfig;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.projectile.path.AccelerateProjectilePath;
import bassebombecraft.operator.projectile.path.CircleProjectilePath;
import bassebombecraft.operator.projectile.path.DeaccelerateProjectilePath;
import bassebombecraft.operator.projectile.path.RandomProjectilePath;
import bassebombecraft.operator.projectile.path.SineProjectilePath;
import bassebombecraft.operator.projectile.path.ZigZagProjectilePath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Generic projectile implementation to support composite items.
 * 
 * This is a super class for actual projectile implementations.
 */
public class GenericCompositeProjectileEntity extends Entity implements IProjectile {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = GenericCompositeProjectileEntity.class.getSimpleName();

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
	Ports projectileModifierPorts;

	/**
	 * Projectile particle rendering ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports addParticlesPorts;

	/**
	 * Projectile entity configuration.
	 */
	ProjectileEntityConfig projectileConfig;

	/**
	 * Particle info for rendering.
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Client side projectile generator operator.
	 */
	Operator2 addParticlesOp;

	/**
	 * Constructor
	 * 
	 * @param type   entity type.
	 * @param world  world object.
	 * @param config projectile entity configuration.
	 */
	public GenericCompositeProjectileEntity(EntityType<?> type, World world, ProjectileEntityConfig config) {
		super(type, world);
		projectileConfig = config;
		infos = createFromConfig(projectileConfig.particles);
		addParticlesOp = new AddParticlesFromPosAtClient2(infos);
		duration = genericProjectileEntityProjectileDuration.get();
		projectileModifierPorts = getInstance();
		addParticlesPorts = getInstance();
		initialiseDuration();
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 * @param config  projectile entity configuration.
	 */
	public GenericCompositeProjectileEntity(EntityType<?> type, LivingEntity invoker, ProjectileEntityConfig config) {
		this(type, invoker.getEntityWorld(), config);
		this.setPosition(invoker.getPosX(), invoker.getPosYEye() - 0.1, invoker.getPosZ());
		this.invokerUUID = invoker.getUniqueID();
		this.invoker = invoker;
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

		// calculate motion
		Vec3d motionVector = (new Vec3d(x, y, z)).normalize()
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
	public void doShoot(Vec3d orientation) {
		setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		double force = projectileConfig.force.get();
		double inaccuracy = projectileConfig.inaccuracy.get();
		double velocity = force * orientation.length();
		shoot(orientation.getX(), orientation.getY(), orientation.getZ(), (float) velocity, (float) inaccuracy);
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

	@Override
	public void tick() {
		super.tick();
		try {
			// calculate collision with block or entity
			RayTraceResult result = calculateCollision();

			// if hit then process collision
			if (result.getType() != RayTraceResult.Type.MISS)
				onImpact(result);

			// process projectile modifiers
			processCompositeModifiers();

			// Update motion and position
			updateMotionAndPosition();

			// send particle rendering info to client
			addParticles();

			// update ports counter
			projectileModifierPorts.incrementCounter();

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

		// TODO: this must be made condition to support drilling projectiles
		// remove particle from server world
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
	 * Update motion and position of the projectile.
	 */
	void updateMotionAndPosition() {
		Vec3d motionVec = this.getMotion();
		Vec3d positionVec = this.getPositionVec();

		// calculate motion drag
		// TODO: Add as property
		float motionScale = this.isInWater() ? getWaterDrag() : getAirDrag();

		// calculate motion and position
		Vec3d nextMotionVec = motionVec.scale(motionScale);
		Vec3d nextPositionVec = motionVec.add(positionVec);		
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
	 * Add particle on update tick.
	 */
	void addParticles() {

		// exit if particles shouldn't be spawned in this tick
		FrequencyRepository frequencyRepository = getProxy().getClientFrequencyRepository();
		if (!frequencyRepository.isActive(PARTICLE_SPAWN_FREQUENCY))
			return;

		addParticlesPorts.setBlockPosition1(getPosition());
		run(addParticlesPorts, addParticlesOp);
	}

}
