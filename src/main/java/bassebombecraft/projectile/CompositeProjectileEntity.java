package bassebombecraft.projectile;

import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;

import java.util.Set;
import java.util.UUID;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.path.RandomProjectilePath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Prototype projectile
 * 
 * TODO: Add description.
 */
public class CompositeProjectileEntity extends Entity implements IProjectile {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = CompositeProjectileEntity.class.getSimpleName();

	/**
	 * Invoker UUID.
	 */
	UUID invokerUUID;

	/**
	 * Projectile invoker
	 */
	protected LivingEntity invoker;

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public CompositeProjectileEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 */
	public CompositeProjectileEntity(EntityType<?> type, LivingEntity invoker, World world) {
		this(type, world);
		this.setPosition(invoker.getPosX(), invoker.getPosYEye() - 0.1, invoker.getPosZ());
		this.invokerUUID = invoker.getUniqueID();
		this.invoker = invoker;
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

		// add noise
		// TODO: determine should noise be remove?
		Vec3d vec3d = (new Vec3d(x, y, z)).normalize()
				.add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
				.scale(velocity);
		this.setMotion(vec3d);

		// calculate yaw and pitch for projectile
		float f = MathHelper.sqrt(horizontalMag(vec3d));
		this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	public void tick() {
		super.tick();

		// calculate if collision with block or entitys
		RayTraceResult rayTraceResult = ProjectileHelper.rayTrace(this,
				this.getBoundingBox().expand(this.getMotion()).grow(1),
				entity -> !entity.isSpectator() && entity.canBeCollidedWith() && entity != this.invoker,
				RayTraceContext.BlockMode.COLLIDER, true);

		if (rayTraceResult.getType() != RayTraceResult.Type.MISS) {

			// TOOD: logic on hit entity

			if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {

				Entity entityHit = ((EntityRayTraceResult) rayTraceResult).getEntity();

				if (entityHit.getUniqueID().equals(this.invokerUUID))
					return;

				// TODO: add damage xxxx.damageCollection().causeDamage(this, entityHit);
			}
			if (!this.world.isRemote())
				this.remove();

		}

		// process projectile modifiers
		processModifiers();

		// Update motion and postion
		updateMotion();
	}

	/**
	 * Process defined projectile modifiers.
	 */
	void processModifiers() {

		// get tags
		Set<String> tags = this.getTags();

		// exit if no tags is defined
		if (tags.isEmpty())
			return;

		// handle: teleport invoker
		if (tags.contains(RandomProjectilePath.NAME))
			calculateRandomPath();

	}

	/**
	 * Execute random path operator.
	 */
	void calculateRandomPath() {

		Operator2 operator = new RandomProjectilePath();

		// create ports
		Ports ports = getInstance();
		ports.setEntity1(this);

		// execute
		run(ports, operator);
	}

	/**
	 * Update motion and position of the projectile.
	 */
	void updateMotion() {
		Vec3d motionVec = this.getMotion();
		Vec3d positionVec = this.getPositionVec();

		// calculate motion drag
		// TODO: Add as property
		float motionScale = this.isInWater() ? getWaterDrag() : getAirDrag();

		// calculate motion and position
		Vec3d nextMotionVec = motionVec.scale(motionScale);
		Vec3d nextPositionVec = motionVec.add(positionVec);

		// update motion and position
		this.setMotion(nextMotionVec.getX(), nextMotionVec.getY() - this.getGravity(), nextMotionVec.getZ());
		this.setPosition(nextPositionVec.getX(), nextPositionVec.getY(), nextPositionVec.getZ());
	}

	@Override
	protected void registerData() {
		// NO-OP

		// TODO: investigate this method
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
