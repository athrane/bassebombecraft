package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;
import static net.minecraft.util.math.RayTraceResult.Type.ENTITY;

import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a hole the
 * size of the entity hit box beneath the hit mob.
 * 
 * If a block is hit then a small is created where the projectile hit.
 */
public class DigMobHole implements ProjectileAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = DigMobHole.class.getSimpleName();

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
		Config configuration = getBassebombeCraft().getConfiguration();

		noHitHoleDepth = configuration.getInt(CONFIG_KEY + ".NoHitHoleDepth");
		noHitholeHeight = configuration.getInt(CONFIG_KEY + ".NoHitHoleHeight");
		noHitholeWidth = configuration.getInt(CONFIG_KEY + ".NoHitHoleWidth");
		holeHeightExpansion = configuration.getInt(CONFIG_KEY + ".HitHoleHeightExpansion");
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		CompositeStructure composite = new CompositeStructure();

		// dig a small hole if block was hit
		if (movObjPos.typeOfHit.equals(BLOCK)) {

			// calculate set of block directives
			BlockPos offset = calculatePosition(world, movObjPos);

			createVerticalStructure(composite);
			PlayerDirection playerDirection = PlayerDirection.South;
			List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite);

			// add directives
			repository.addAll(directives);
		}

		// dig hole around mob if mob was hit
		if (movObjPos.typeOfHit.equals(ENTITY)) {

			Entity entity = movObjPos.entityHit;
			AxisAlignedBB aabb = entity.getEntityBoundingBox();		
			BlockPos min = new BlockPos(aabb.minX, aabb.minY - holeHeightExpansion , aabb.minZ);
			BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
			for (BlockPos pos : BlockPos.getAllInBox(min, max)) {
				double translateY = aabb.maxY - aabb.minY;
				BlockPos tranlatedPos = pos.add(0, -translateY, 0);
				BlockDirective directive = new BlockDirective(tranlatedPos, Blocks.AIR, DONT_HARVEST);
				repository.add(directive);
			}
		}
	}

	/**
	 * Calculate position.
	 * 
	 * @param world
	 *            world object.
	 * 
	 * @param movObjPos
	 *            hit object.
	 * 
	 * @return position where block should be spawned.
	 */
	BlockPos calculatePosition(World world, RayTraceResult movObjPos) {
		switch (movObjPos.sideHit) {

		case UP:
			return movObjPos.getBlockPos().up();

		case DOWN:
			return movObjPos.getBlockPos().down();

		case SOUTH:
			return movObjPos.getBlockPos().south();

		case NORTH:
			return movObjPos.getBlockPos().north();

		case EAST:
			return movObjPos.getBlockPos().east();

		case WEST:
			return movObjPos.getBlockPos().west();

		default:
			return movObjPos.getBlockPos().up();
		}
	}

	/**
	 * Add created vertical structure to composite.
	 * 
	 * @param composite
	 *            composite structure
	 */
	void createVerticalStructure(CompositeStructure composite) {
		BlockPos offset = new BlockPos(0, -noHitHoleDepth, 0);
		BlockPos size = new BlockPos(noHitholeWidth, noHitholeHeight, noHitHoleDepth);
		composite.add(createAirStructure(offset, size));
	}

}
