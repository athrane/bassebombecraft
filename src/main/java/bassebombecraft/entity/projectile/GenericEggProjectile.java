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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Projectile which executes operator on impact.
 * 
 * @deprecated Replace with {@linkplain GenericCompositeProjectileEntity}.
 */
@Deprecated
public class GenericEggProjectile extends ThrowableItemProjectile {

	/**
	 * Null behaviour.
	 */
	static final ProjectileAction NULL_BEHAVIOUR = new NullAction();

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 5;
	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.INSTANT_EFFECT;
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
	public GenericEggProjectile(EntityType<? extends ThrowableItemProjectile> type, Level world) {
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
	public GenericEggProjectile(Level world, LivingEntity entity, ProjectileAction behaviour) {
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
	protected void onHit(HitResult result) {

		// exit if on client side
		if (isLogicalClient(getCommandSenderWorld()))
			return;

		try {
			// execute behaviour
			action.execute(this, level, result);

			// send particle rendering info (for impact particle) to client
			ParticleRendering particle = getInstance(blockPosition(), PARTICLE_INFO);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);

			// remove this projectile
			remove(Entity.RemovalReason.DISCARDED);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	public void tick() {
		super.tick();

		try {
			// send particle rendering info to client
			ParticleRendering particle = getInstance(blockPosition(), PARTICLE_INFO);
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
		Vec3 vector3d = (new Vec3(x, y, z)).normalize()
				.add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
						this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
				.scale((double) velocity);
		this.setDeltaMovement(vector3d);
		float f = Mth.sqrt(getHorizontalDistanceSqr(vector3d));
		float yRot = (float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
		float xRot = (float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
		setYRot(yRot);
		setXRot(xRot);
		this.yRotO = yRot;
		this.xRotO = xRot;
	}

}