package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.config.ModConfiguration.digMobHoleHeightExpansion;
import static bassebombecraft.config.ModConfiguration.digMobHoleNoHitHoleDepth;
import static bassebombecraft.config.ModConfiguration.digMobHoleNoHitHoleHeight;
import static bassebombecraft.config.ModConfiguration.digMobHoleNoHitHoleWidth;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.player.PlayerDirection.South;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static net.minecraft.block.Blocks.AIR;

import java.util.List;
import java.util.function.Function;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns a hole
 * the size of the entity hit box beneath the hit mob.
 * 
 * If a block is hit then a small hole is created where the projectile hit.
 */
public class DigMobHole2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = DigMobHole2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld;

	/**
	 * No hit hole depth.
	 */
	final int noHitHoleDepth;

	/**
	 * No hit hole height.
	 */
	final int noHitholeHeight;

	/**
	 * No hit hole width.
	 */
	final int noHitholeWidth;

	/**
	 * Hole height expansion.
	 */
	final int holeHeightExpansion;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetWorld        function to get world.
	 */
	public DigMobHole2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetWorld = fnGetWorld;

		noHitHoleDepth = digMobHoleNoHitHoleDepth.get();
		noHitholeHeight = digMobHoleNoHitHoleHeight.get();
		noHitholeWidth = digMobHoleNoHitHoleWidth.get();
		holeHeightExpansion = digMobHoleHeightExpansion.get();
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 */
	public DigMobHole2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);

		// get world
		World world = fnGetWorld.apply(ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// teleport to hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return ports;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get entity aabb and convert it into air blocks
			AxisAlignedBB aabb = entity.getBoundingBox();
			BlockPos min = new BlockPos(aabb.minX, aabb.minY - holeHeightExpansion, aabb.minZ);
			BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
			BlockPos.getAllInBox(min, max).forEach(pos -> registerBlockToDig(aabb, pos, world));
			
			return ports;
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return ports;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// calculate set of block directives
			BlockPos offset = calculatePosition(blockResult);
			CompositeStructure composite = new CompositeStructure();
			createVerticalStructure(composite);
			PlayerDirection playerDirection = South;
			List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite, DONT_HARVEST,
					world);

			// add directives
			BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
			repository.addAll(directives);
		}

		return ports;
	}

	/**
	 * Register block for processed to generate air block.
	 * 
	 * @param aabb  AABB
	 * @param pos   block position to process.
	 * @param world world where directive should be processed.
	 */
	void registerBlockToDig(AxisAlignedBB aabb, BlockPos pos, World world) {
		double translateY = aabb.maxY - aabb.minY;
		BlockPos tranlatedPos = pos.add(0, -translateY, 0);
		BlockDirective directive = getInstance(tranlatedPos, AIR, DONT_HARVEST, world);
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);
	}

	/**
	 * Add created vertical structure to composite.
	 * 
	 * @param composite composite structure
	 */
	void createVerticalStructure(CompositeStructure composite) {
		BlockPos offset = new BlockPos(0, -noHitHoleDepth, 0);
		BlockPos size = new BlockPos(noHitholeWidth, noHitholeHeight, noHitHoleDepth);
		composite.add(createAirStructure(offset, size));
	}

}
