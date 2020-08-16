package bassebombecraft.entity.projectile;

import static bassebombecraft.BassebombeCraft.getProxy;
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

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.path.AccelerateProjectilePath;
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
 * this is a super class for actual projectile implementations.
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
	 * Zig Zag projectile path operator.
	 */
	static final Operator2 ZIGZAG_PATH_OPERATOR = new ZigZagProjectilePath();

	/**
	 * Sine projectile path operator.
	 */
	static final Operator2 SINE_PATH_OPERATOR = new SineProjectilePath();

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
	 * Projectile moddifier ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports projectileModifierPorts;

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public GenericCompositeProjectileEntity(EntityType<?> type, World world) {
		super(type, world);
		duration = genericProjectileEntityProjectileDuration.get();
		projectileModifierPorts = getInstance();
		initialiseDuration();
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 */
	public GenericCompositeProjectileEntity(EntityType<?> type, LivingEntity invoker, World world) {
		this(type, world);
		this.setPosition(invoker.getPosX(), invoker.getPosYEye() - 0.1, invoker.getPosZ());
		this.invokerUUID = invoker.getUniqueID();
		this.invoker = invoker;
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

		// add noise
		// TODO: eliminate inaccuracy
		/**
		 * Vec3d vec3d = (new Vec3d(x, y, z)).normalize() .add(this.rand.nextGaussian()
		 * * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double)
		 * 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F *
		 * (double) inaccuracy) .scale(velocity);
		 **/

		// calculate motion
		Vec3d motionVector = new Vec3d(x, y, z).normalize().scale(velocity);
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

		// calculate collision with block or entity
		RayTraceResult result = calculateCollision();

		// if hit then process collision
		if (result.getType() != RayTraceResult.Type.MISS)
			onImpact(result);

		// process projectile modifiers
		processCompositeModifiers();

		// Update motion and position
		updateMotionAndPosition();

		// TODO: Add particles..

		// update ports counter
		projectileModifierPorts.incrementCounter();
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
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// TODO: should invoker be immune to projectile hits?
			if (entity.getUniqueID().equals(this.invokerUUID))
				return;

			// TODO: add damage xxxx.damageCollection().causeDamage(this, entityHit);
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

		// handle: zig zag
		if (tags.contains(ZigZagProjectilePath.NAME))
			calculateZigZagPath();

		// handle: sine
		if (tags.contains(SineProjectilePath.NAME))
			calculateSinePath();

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

		// TODO: Include gravity in motion calculation for "physical projectile"
		// update motion and position
		// this.setMotion(nextMotionVec.getX(), nextMotionVec.getY() -
		// this.getGravity(), nextMotionVec.getZ());
		this.setMotion(nextMotionVec.getX(), nextMotionVec.getY(), nextMotionVec.getZ());
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

	float getGravity() {
		return 0.01f;
	}

}
