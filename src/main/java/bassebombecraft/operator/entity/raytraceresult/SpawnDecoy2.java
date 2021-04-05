package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.ModConstants.DECOY;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.config.ModConfiguration.spawnDecoyKnockBackResistance;
import static bassebombecraft.config.ModConfiguration.spawnDecoyMaxHealth;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getBcSetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;
import static net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH;
import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which spawn a decoy
 * (2D Panda). The decoy is spawned at the hit entity or block .
 */
public class SpawnDecoy2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnDecoy2.class.getSimpleName();

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to set decoy entity (at ports).
	 */
	BiConsumer<Ports, LivingEntity> bcSetDecoy;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker        function to get invoker entity.
	 * @param fnGetRayTraceResult function to get ray trace result.
	 * @param bcSetDecoy          function to set decoy entity.
	 */
	public SpawnDecoy2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, RayTraceResult> fnGetRayTraceResult,
			BiConsumer<Ports, LivingEntity> bcSetDecoy) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.bcSetDecoy = bcSetDecoy;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured to set created decoy as living entity #2 in the ports.
	 */
	public SpawnDecoy2() {
		this(getFnGetLivingEntity1(), getFnGetRayTraceResult1(), getBcSetLivingEntity2());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		RayTraceResult result = applyV(fnGetRayTraceResult, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare
		BlockPos spawnPosition = null;

		// spawn at hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result;
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position
			spawnPosition = entity.getPosition();
		}

		// spawn at hit block
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get block position
			spawnPosition = calculatePosition(blockResult);
		}

		// get world
		World world = invoker.getEntityWorld();

		// create entity
		PandaEntity entity = EntityType.PANDA.create(world);
		entity.setLocationAndAngles(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(),
				invoker.rotationYaw, invoker.rotationPitch);

		// set entity attributes
		setAttribute(entity, MOVEMENT_SPEED, 0);
		setAttribute(entity, MAX_HEALTH, spawnDecoyMaxHealth.get());
		setAttribute(entity, KNOCKBACK_RESISTANCE, spawnDecoyKnockBackResistance.get());
		setAttribute(entity, DECOY, 1.0D);

		// AI not set

		// spawn
		world.addEntity(entity);

		// store decoy entity
		bcSetDecoy.accept(ports, entity);
	}

}
