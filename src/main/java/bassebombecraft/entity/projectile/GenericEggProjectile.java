package bassebombecraft.entity.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.projectile.action.NullAction;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Projectile which executes operator on impact.
 * 
 * @deprecated Replace with {@linkplain GenericCompositeProjectileEntity}.
 */
@Deprecated
public class GenericEggProjectile extends ProjectileItemEntity {

	/**
	 * Null behaviour.
	 */
	static final ProjectileAction NULL_BEHAVIOUR = new NullAction();

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 5;
	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.INSTANT_EFFECT;
	static final int PARTICLE_DURATION = 20; // Measured in world ticks
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	/**
	 * Entity name.
	 */
	public static final String NAME = GenericEggProjectile.class.getSimpleName();

	/**
	 * Action, initial null.
	 */
	ProjectileAction action = NULL_BEHAVIOUR;

	/**
	 * GenericEggProjectile constructor
	 * 
	 * Supports initialization in class {@linkplain EntityTypeRegistryEventHandler}.
	 * 
	 * @param type  entity type.
	 * @param world world.
	 */
	public GenericEggProjectile(EntityType<? extends ProjectileItemEntity> type, World world) {
		super(type, world);
		setBehaviour(NULL_BEHAVIOUR);
	}

	/**
	 * GenericEggProjectile constructor.
	 * 
	 * @param world     world object.
	 * @param entity    projectile thrower.
	 * @param behaviour projectile behaviour.
	 */
	public GenericEggProjectile(World world, LivingEntity entity, ProjectileAction behaviour) {
		super(EntityType.EGG, entity, world);
		setBehaviour(behaviour);
	}

	/**
	 * Sets action.
	 * 
	 * @param action projectile action.
	 */
	public void setBehaviour(ProjectileAction action) {
		this.action = action;
	}

	/**
	 * Called when this ThrowableEntity hits a block or entity.
	 */
	@Override
	protected void onImpact(RayTraceResult result) {

		// exit if on client side
		if (isLogicalClient(getEntityWorld()))
			return;

		try {
			// execute behaviour
			action.execute(this, world, result);

			// send particle rendering info (for impact particle) to client
			ParticleRendering particle = getInstance(getPosition(), PARTICLE_INFO);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);

			// remove this projectile
			remove();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	public void tick() {
		super.tick();

		try {
			// send particle rendering info to client
			ParticleRendering particle = getInstance(getPosition(), PARTICLE_INFO);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	protected Item getDefaultItem() {
		return Items.EGG;
	}

	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		Vector3d vector3d = (new Vector3d(x, y, z)).normalize()
				.add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
				.scale((double) velocity);
		this.setMotion(vector3d);
		float f = MathHelper.sqrt(horizontalMag(vector3d));
		this.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

}