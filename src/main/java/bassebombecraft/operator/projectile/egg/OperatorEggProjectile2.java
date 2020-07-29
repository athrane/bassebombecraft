package bassebombecraft.operator.projectile.egg;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.projectile.EntityTypeRegistryEventHandler;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.NullOp2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
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
 * Generic projectile implementation which executes embedded operator
 * implementing the {@linkplain Operator2} interface.
 * 
 * The ports is created on projectile impact.
 * 
 * The ports is configured with the ray tracing result (from the impact event) and the thrower entity.
 */
public class OperatorEggProjectile2 extends ProjectileItemEntity {

	// TODO: use ModConfiguration

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 2;
	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.INSTANT_EFFECT;
	static final int PARTICLE_DURATION = 5; // Measured in world ticks
	static final double PARTICLE_SPEED = 0.1;
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	/**
	 * Entity identifier.
	 */
	public static final String PROJECTILE_NAME = OperatorEggProjectile2.class.getSimpleName();

	/**
	 * Operator.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param entity   projectile thrower.
	 * @param operator operator to execute on impact.
	 */
	public OperatorEggProjectile2(LivingEntity entity, Operator2 operator) {
		super(EntityType.EGG, entity, entity.getEntityWorld());
		this.operator = operator;
	}

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
	public OperatorEggProjectile2(EntityType<? extends ProjectileItemEntity> type, World world) {
		super(type, world);
		this.operator = new NullOp2();
	}
	

	@Override
	protected void onImpact(RayTraceResult result) {

		// exit if on client side
		if (isLogicalClient(getEntityWorld()))
			return;

		try {

			// create ports
			Ports ports = DefaultPorts.getInstance();
			ports.setRayTraceResult1(result);
			ports.setLivingEntity1(this.getThrower());

			// execute operator
			Operators2.run(ports, operator);

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