package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerDirection.South;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static net.minecraft.block.Blocks.AIR;

import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a hole the
 * size of the entity hit box beneath the hit mob.
 * 
 * If a block is hit then a small hole is created where the projectile hit.
 */
public class DigMobHole implements ProjectileAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = DigMobHole.class.getSimpleName();

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository repository;

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
	 * DigMobHole constructor.
	 */
	public DigMobHole() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
		noHitHoleDepth = ModConfiguration.digMobHoleNoHitHoleDepth.get();
		noHitholeHeight = ModConfiguration.digMobHoleNoHitHoleHeight.get();
		noHitholeWidth = ModConfiguration.digMobHoleNoHitHoleWidth.get();
		holeHeightExpansion = ModConfiguration.digMobHoleHeightExpansion.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// dig a small hole if block was hit
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// calculate set of block directives
			BlockPos offset = calculatePosition(blockResult);
			CompositeStructure composite = new CompositeStructure();
			createVerticalStructure(composite);
			PlayerDirection playerDirection = South;
			List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite);

			// add directives
			repository.addAll(directives);

			return;
		}

		// exit if entity isn't hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// get entity aabb and convert it into air blocks
		AxisAlignedBB aabb = entity.getBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY - holeHeightExpansion, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		BlockPos.getAllInBox(min, max).forEach(pos -> registerBlockToDig(aabb, pos));
	}

	/**
	 * Register block for processed to generate air block.
	 * 
	 * @param aabb AABB
	 * @param pos  block position to process.
	 */
	void registerBlockToDig(AxisAlignedBB aabb, BlockPos pos) {
		double translateY = aabb.maxY - aabb.minY;
		BlockPos tranlatedPos = pos.add(0, -translateY, 0);
		BlockDirective directive = new BlockDirective(tranlatedPos, AIR, DONT_HARVEST);
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
