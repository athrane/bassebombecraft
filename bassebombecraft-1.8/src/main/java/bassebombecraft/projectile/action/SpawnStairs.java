package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.entity.EntityUtils.calculateEntityFeetPosititionAsInt;
import static bassebombecraft.entity.EntityUtils.getPlayerDirection;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.item.action.build.BuildUtils.addSolidStairUp;
import static bassebombecraft.item.action.build.BuildUtils.createInstance;

import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.build.tower.StairsMaterial;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a stairs
 * to the hit block.
 * 
 * If a mob is hit then NO-OP.
 */
public class SpawnStairs implements ProjectileAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = SpawnStairs.class.getSimpleName();

	/**
	 * duration of stairs.
	 */
	int duration;

	/**
	 * SpawnIceStairs constructor.
	 */
	public SpawnStairs() {
		Config configuration = getBassebombeCraft().getConfiguration();
		duration = configuration.getInt(CONFIG_KEY + ".Duration");
	}

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// spawn a temporary lava block if no entity was hit
		if (movObjPos.entityHit == null) {

			// get thrower
			EntityLivingBase owner = projectile.getThrower();

			// get thrower feet position
			int yFeetPosition = calculateEntityFeetPosititionAsInt(owner);

			// calculate target position
			BlockPos targetPosition = calculatePosition(world, movObjPos);

			// calculate stairs height
			int stairsHeight = Math.abs(yFeetPosition - targetPosition.getY()) + 1;

			// calculate stairs offset
			BlockPos stairOffset = new BlockPos(0, -stairsHeight, 1 - stairsHeight);

			// create material
			CompositeStructure composite = new CompositeStructure();
			IBlockState state = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING,
					EnumFacing.SOUTH);
			StairsMaterial stairsMaterial = createInstance(state, Blocks.STONE_BRICK_STAIRS, Blocks.STONEBRICK);

			// create stairs structure
			addSolidStairUp(stairsHeight, stairsMaterial, composite, stairOffset);

			// get owner direction
			PlayerDirection playerDirection = getPlayerDirection(owner);

			// calculate set of block directives
			BlockPos offset = targetPosition;
			List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite);

			// set blocks
			for (BlockDirective directive : directives) {
				setTemporaryBlock(world, directive, duration);
			}

			return;
		}

		// spawn temporary ice block around the hit mob
		// NO-OP

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

}
