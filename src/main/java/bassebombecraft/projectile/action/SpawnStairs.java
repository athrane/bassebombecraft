package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.entity.EntityUtils.calculateEntityFeetPosititionAsInt;
import static bassebombecraft.entity.EntityUtils.getPlayerDirection;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.item.action.build.BuildUtils.addSolidStairUp;
import static bassebombecraft.item.action.build.BuildUtils.createInstance;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static net.minecraft.block.Blocks.STONE_BRICKS;
import static net.minecraft.block.Blocks.STONE_BRICK_STAIRS;
import static net.minecraft.util.Direction.SOUTH;

import java.util.List;

import bassebombecraft.block.BlockUtils;
import bassebombecraft.config.ModConfiguration;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.build.tower.StairsMaterial;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a stairs to
 * the hit block.
 * 
 * If a mob is hit then NO-OP.
 */
public class SpawnStairs implements ProjectileAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = SpawnStairs.class.getSimpleName();
	
	/**
	 * duration of stairs.
	 */
	int duration;

	/**
	 * SpawnIceStairs constructor.
	 */
	public SpawnStairs() {
		duration = ModConfiguration.spawnStairsDuration.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if no block was hit
		if (!isBlockHit(result))
			return;

		// exit if result isn't block ray trace result
		if (!isTypeBlockRayTraceResult(result))
			return;

		// type cast
		BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

		// get thrower
		LivingEntity owner = projectile.getThrower();

		// get thrower feet position
		int yFeetPosition = calculateEntityFeetPosititionAsInt(owner);

		// calculate target position
		BlockPos targetPosition = BlockUtils.calculatePosition(blockResult);

		// calculate stairs height
		int stairsHeight = Math.abs(yFeetPosition - targetPosition.getY()) + 1;

		// calculate stairs offset
		BlockPos stairOffset = new BlockPos(0, -stairsHeight, 1 - stairsHeight);

		// create material
		CompositeStructure composite = new CompositeStructure();
		BlockState state = STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, SOUTH);
		StairsMaterial stairsMaterial = createInstance(state, STONE_BRICK_STAIRS, STONE_BRICKS);

		// create stairs structure
		addSolidStairUp(stairsHeight, stairsMaterial, composite, stairOffset);

		// get owner direction
		PlayerDirection playerDirection = getPlayerDirection(owner);

		// calculate set of block directives
		BlockPos offset = targetPosition;
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, composite, world);

		// set blocks
		for (BlockDirective directive : directives) {
			setTemporaryBlock(world, directive, duration);
		}

	}

}
