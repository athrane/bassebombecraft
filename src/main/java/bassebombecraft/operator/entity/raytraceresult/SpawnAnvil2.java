package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ModConfiguration.spawnAnvilOffset;
import static bassebombecraft.config.ModConfiguration.spawnCobwebDuration;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import static net.minecraft.block.Blocks.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which which spawns an
 * anvil over the top of the hit entity.
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
	public SpawnAnvil2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld) {
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

			// get entity position
			Vec3d posVec = entity.getPositionVector();
			float height = entity.getHeight();
			double lx = posVec.x;
			double ly = posVec.y + height + spawnAnvilOffset.get();
			double lz = posVec.z;

			BlockPos blockpos = new BlockPos(lx, ly, lz);
			setTemporaryBlock(world, blockpos, ANVIL, spawnCobwebDuration.get());
		}

		return ports;
	}

}
