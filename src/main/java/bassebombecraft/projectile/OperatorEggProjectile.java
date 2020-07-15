package bassebombecraft.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.projectile.EntityTypeRegistryEventHandler;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.projectile.OperatorEggProjectile2;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain ProjectileItemEntity} which executes embedded
 * operators.
 * 
 * @deprecated Replace with {@linkplain OperatorEggProjectile2}.
 */
@Deprecated
public class OperatorEggProjectile extends ProjectileItemEntity {

	/**
	 * Null Operators.
	 */
	static final Operators NULL_OPS = new Operators();

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
	public static final String PROJECTILE_NAME = OperatorEggProjectile.class.getSimpleName();

	/**
	 * Operators, initially null.
	 */
	Operators operators = NULL_OPS;

	/**
	 * Constructor
	 * 
	 * Supports initialization in class {@linkplain EntityTypeRegistryEventHandler}.
	 * 
	 * Initialises entity with null behaviour.
	 * 
	 * @param type  entity type.
	 * @param world world.
	 */
	public OperatorEggProjectile(EntityType<? extends ProjectileItemEntity> type, World world) {
		super(type, world);
		setOperators(NULL_OPS);
	}

	/**
	 * Constructor constructor.
	 * 
	 * @param world     world object.
	 * @param entity    projectile thrower.
	 * @param operators operators to execute on impact.
	 */
	public OperatorEggProjectile(World world, LivingEntity entity, Operators operators) {
		super(EntityType.EGG, entity, world);
		setOperators(operators);
	}

	/**
	 * Sets operators.
	 * 
	 * @param operators operators to execute on impact.
	 */
	public void setOperators(Operators operators) {
		this.operators = operators;
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
			// execute operators
			operators.run(owner, result);

			// send particle rendering info to client
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

}