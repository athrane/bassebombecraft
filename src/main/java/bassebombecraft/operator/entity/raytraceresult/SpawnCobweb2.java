package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ModConfiguration.spawnCobwebDuration;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.world.level.block.Blocks.COBWEB;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns a cobweb
 * around the hit mob.
 * 
 * If a block is hit then a temporary cobweb is created where the projectile
 * hit.
 */
public class SpawnCobweb2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnCobweb2.class.getSimpleName();

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
	public SpawnCobweb2(Function<Ports, HitResult> fnGetRayTraceResult, Function<Ports, Level> fnGetWorld) {
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
	public SpawnCobweb2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);
		Level world = applyV(fnGetWorld, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// spawn cobweb around entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityHitResult) result).getEntity();

			// get entity aabb and convert it into cobweb blocks
			AABB aabb = entity.getBoundingBox();
			BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
			BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
			BlockPos.betweenClosedStream(min, max)
					.forEach(pos -> setTemporaryBlock(world, pos, COBWEB, spawnCobwebDuration.get()));
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockHitResult blockResult = (BlockHitResult) result;

			// spawn block
			BlockPos spawnPosition = calculatePosition(blockResult);
			setTemporaryBlock(world, spawnPosition, COBWEB, spawnCobwebDuration.get());
		}
	}

}
