package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ModConfiguration.spawnAnvilDuration;
import static bassebombecraft.config.ModConfiguration.spawnAnvilOffset;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.world.level.block.Blocks.ANVIL;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns an anvil
 * over the top of the hit entity.
 * 
 * If a block is hit then NO-OP.
 */
public class SpawnAnvil2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnAnvil2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, Level> fnGetWorld;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetWorld        function to get world.
	 */
	public SpawnAnvil2(Function<Ports, HitResult> fnGetRayTraceResult, Function<Ports, Level> fnGetWorld) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetWorld = fnGetWorld;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 */
	public SpawnAnvil2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);
		Level world = applyV(fnGetWorld, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// if entity is hit drop anvil on entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityHitResult) result).getEntity();

			// get entity position
			Vec3 posVec = entity.position();
			float height = entity.getBbHeight();
			double lx = posVec.x;
			double ly = posVec.y + height + spawnAnvilOffset.get();
			double lz = posVec.z;

			BlockPos blockpos = new BlockPos(lx, ly, lz);
			setTemporaryBlock(world, blockpos, ANVIL, spawnAnvilDuration.get());
		}
	}

}
