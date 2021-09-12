package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.config.ModConfiguration.wildfireEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.wildfireEffectDuration;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.potion.effect.RegisteredEffects.WILDFIRE_EFFECT;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns a failed
 * phoenix. The phoenix is spawned at the hit entity or block.
 * 
 * The phoenix is added to the invokers team.
 */
public class SpawnFlamingChicken2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnFlamingChicken2.class.getSimpleName();

	/**
	 * Spawn Y offset.
	 */
	static final int Y_SPAWN_OFFSET = 2;

	/**
	 * Squid pitch.
	 */
	static final float PITCH = 0.0F;

	/**
	 * Entity yaw.
	 */
	static final float PARTIAL_TICKS = 1.0F;

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker      function to get invoker entity.
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public SpawnFlamingChicken2(Function<Ports, LivingEntity> fnGetInvoker,
			Function<Ports, RayTraceResult> fnGetRayTraceResult) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetRayTraceResult = fnGetRayTraceResult;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 */
	public SpawnFlamingChicken2() {
		this(getFnGetLivingEntity1(), getFnGetRayTraceResult1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		RayTraceResult result = applyV(fnGetRayTraceResult, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare variables
		BlockPos spawnPosition = null;
		float yaw = 0;

		// spawn cobweb around entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get entity position
			spawnPosition = entity.getPosition();

			// get entity yaw
			yaw = entity.getYaw(PARTIAL_TICKS);
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// calculate spawn position
			spawnPosition = calculatePosition(blockResult);
		}

		// get world
		World world = invoker.getEntityWorld();

		// spawn chicken
		ChickenEntity entity = EntityType.CHICKEN.create(world);
		entity.setLocationAndAngles(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(), yaw, PITCH);
		world.addEntity(entity);

		// create and add effect instance
		entity.addPotionEffect(createEffect());

		// register chicken on invokers team
		getProxy().getServerTeamRepository().add((LivingEntity) invoker, entity);
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		int duration = wildfireEffectDuration.get();
		int amplifier = wildfireEffectAmplifier.get();
		return new EffectInstance(WILDFIRE_EFFECT.get(), duration, amplifier);
	}

}
