package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ModConfiguration.spawnLavaBlockDuration;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static net.minecraft.block.Blocks.LAVA;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns an lava
 * block around the hit mob.
 * 
 * If a block is hit then a temporary lava block is created where the projectile
 * hit.
 */
public class SpawnLavaBlock2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnLavaBlock2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetWorld        function to get world.
	 */
	public SpawnLavaBlock2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld) {
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
	public SpawnLavaBlock2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);
		if (result == null)
			return ports;

		// get world
		World world = fnGetWorld.apply(ports);
		if (world == null)
			return ports;

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// spawn cobweb around entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return ports;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get entity aabb and convert it into cobweb blocks
			AxisAlignedBB aabb = entity.getBoundingBox();
			BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
			BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
			BlockPos.getAllInBox(min, max)
					.forEach(pos -> setTemporaryBlock(world, pos, LAVA, spawnLavaBlockDuration.get()));

			return ports;
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return ports;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// spawn block
			BlockPos spawnPosition = calculatePosition(blockResult);
			setTemporaryBlock(world, spawnPosition, LAVA, spawnLavaBlockDuration.get());
		}

		return ports;
	}

}
