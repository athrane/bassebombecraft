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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

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
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker      function to get invoker entity.
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public SpawnFlamingChicken2(Function<Ports, LivingEntity> fnGetInvoker,
			Function<Ports, HitResult> fnGetRayTraceResult) {
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
		HitResult result = applyV(fnGetRayTraceResult, ports);

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
			Entity entity = ((EntityHitResult) result).getEntity();

			// get entity position
			spawnPosition = entity.blockPosition();

			// get entity yaw
			yaw = entity.getViewYRot(PARTIAL_TICKS);
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockHitResult blockResult = (BlockHitResult) result;

			// calculate spawn position
			spawnPosition = calculatePosition(blockResult);
		}

		// get world
		Level world = invoker.getCommandSenderWorld();

		// spawn chicken
		Chicken entity = EntityType.CHICKEN.create(world);
		entity.moveTo(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(), yaw, PITCH);
		world.addFreshEntity(entity);

		// create and add effect instance
		entity.addEffect(createEffect());

		// register chicken on invokers team
		getProxy().getServerTeamRepository().add((LivingEntity) invoker, entity);
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		int duration = wildfireEffectDuration.get();
		int amplifier = wildfireEffectAmplifier.get();
		return new MobEffectInstance(WILDFIRE_EFFECT.get(), duration, amplifier);
	}

}
