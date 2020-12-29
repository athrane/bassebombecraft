package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.ORIGIN_BLOCK_POS;
import static bassebombecraft.config.ModConfiguration.digHoleSize;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.geom.GeometryUtils.convertToPlayerDirection;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.structure.ChildStructure.createAirStructure;

import java.util.List;
import java.util.function.Function;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which dig blocks when
 * projectile hits a block.
 */
public class Dig2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Dig2.class.getSimpleName();

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
	public Dig2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld) {
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
	public Dig2() {
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

		// dig if block is hit
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return ports;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get impact info
			Direction impactDirection = blockResult.getFace();

			// rotate block to impact direction
			Direction rotatedDirection = impactDirection.rotateY().rotateY();
			PlayerDirection playerDirection = convertToPlayerDirection(rotatedDirection);

			// calculate set of block directives
			BlockPos offset = blockResult.getPos();
			CompositeStructure composite = new CompositeStructure();
			createExcavatedStructure(composite);
			List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite, DONT_HARVEST,
					world);

			// add directives
			BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
			repository.addAll(directives);
		}

		return ports;
	}

	/**
	 * Add excavated structure to composite.
	 * 
	 * @param composite composite structure
	 */
	void createExcavatedStructure(CompositeStructure composite) {
		int holeSize = digHoleSize.get();
		BlockPos size = new BlockPos(holeSize, holeSize, holeSize);
		composite.add(createAirStructure(ORIGIN_BLOCK_POS, size));
	}

}
