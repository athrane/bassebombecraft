package bassebombecraft.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.projectile.action.NullAction;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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

	public final static String PROJECTILE_NAME = GenericEggProjectile.class.getSimpleName();

	/**
	 * Behaviour, initial null.
	 */
	ProjectileAction behaviour = NULL_BEHAVIOUR;

	/**
	 * GenericEggProjectile no-arg constructor.
	 * 
	 * Initialises entity with null behaviour.
	 */

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
	 * Sets behaviour.
	 * 
	 * @param behaviour projectile behaviour.
	 */
	public void setBehaviour(ProjectileAction behaviour) {
		this.behaviour = behaviour;
	}

	/**
	 * Called when this ThrowableEntity hits a block or entity.
	 */
	@Override
	protected void onImpact(RayTraceResult result) {

		// get world
		World world = this.getEntityWorld();

		// exit if on client side
		if (isWorldAtClientSide(world))
			return;

		try {
			// execute behaviour
			behaviour.execute(this, world, result);

			// add impact particle for rendering
			ParticleRendering particle = getInstance(getPosition(), PARTICLE_INFO);
			ParticleRenderingRepository repository = getBassebombeCraft().getParticleRenderingRepository();
			repository.add(particle);

			// remove this projectile
			remove();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	protected Item func_213885_i() {
		return Items.EGG;
	}
}