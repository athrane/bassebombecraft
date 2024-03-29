package bassebombecraft.item.action.build;

import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createOakFenceStructure;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraft.world.level.block.Blocks.ANDESITE;
import static net.minecraft.world.level.block.Blocks.BLUE_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.BRICKS;
import static net.minecraft.world.level.block.Blocks.CHISELED_RED_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.CHISELED_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.CHISELED_STONE_BRICKS;
import static net.minecraft.world.level.block.Blocks.COBBLESTONE;
import static net.minecraft.world.level.block.Blocks.CRACKED_STONE_BRICKS;
import static net.minecraft.world.level.block.Blocks.CUT_RED_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.CUT_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.CYAN_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.DIORITE;
import static net.minecraft.world.level.block.Blocks.GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.GREEN_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.LIGHT_BLUE_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.LIGHT_GRAY_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.LIME_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.MAGENTA_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.MAGMA_BLOCK;
import static net.minecraft.world.level.block.Blocks.MOSSY_COBBLESTONE;
import static net.minecraft.world.level.block.Blocks.MOSSY_STONE_BRICKS;
import static net.minecraft.world.level.block.Blocks.NETHER_BRICKS;
import static net.minecraft.world.level.block.Blocks.OAK_LOG;
import static net.minecraft.world.level.block.Blocks.OAK_PLANKS;
import static net.minecraft.world.level.block.Blocks.OBSIDIAN;
import static net.minecraft.world.level.block.Blocks.ORANGE_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.PINK_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.POLISHED_GRANITE;
import static net.minecraft.world.level.block.Blocks.PURPLE_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.RED_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.SANDSTONE;
import static net.minecraft.world.level.block.Blocks.SANDSTONE_STAIRS;
import static net.minecraft.world.level.block.Blocks.SMOOTH_RED_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.SMOOTH_SANDSTONE;
import static net.minecraft.world.level.block.Blocks.SPAWNER;
import static net.minecraft.world.level.block.Blocks.SPRUCE_LOG;
import static net.minecraft.world.level.block.Blocks.STONE;
import static net.minecraft.world.level.block.Blocks.STONE_BRICKS;
import static net.minecraft.world.level.block.Blocks.WHITE_STAINED_GLASS_PANE;
import static net.minecraft.world.level.block.Blocks.YELLOW_STAINED_GLASS_PANE;
import static net.minecraft.core.Direction.*;

import java.util.Random;

import bassebombecraft.item.action.build.tower.BuildMaterial;
import bassebombecraft.item.action.build.tower.StairsMaterial;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import static net.minecraft.world.level.block.StairBlock.*;
import net.minecraft.core.BlockPos;

/**
 * Utility class for build block based structures.
 */
public class BuildUtils {

	/**
	 * Add oak fenced doorway entry turning front (oriented along the x axis).
	 * 
	 * @param structure    structure where door is added to.
	 * @param globalOffset global offset.
	 */
	public static void addMainEntranceFront(Structure structure, BlockPos globalOffset) {
		BlockPos offset = new BlockPos(-2 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		BlockPos size = new BlockPos(5, 3, 1);
		structure.add(createAirStructure(offset, size));

		offset = new BlockPos(-1 + globalOffset.getX(), globalOffset.getY() + 3, globalOffset.getZ());
		size = new BlockPos(3, 2, 1);
		structure.add(createAirStructure(offset, size));

		offset = new BlockPos(globalOffset.getX(), globalOffset.getY() + 5, globalOffset.getZ());
		size = new BlockPos(1, 1, 1);
		structure.add(createAirStructure(offset, size));

		offset = new BlockPos(-3 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ() - 1);
		size = new BlockPos(1, 4, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(-2 + globalOffset.getX(), globalOffset.getY() + 3, globalOffset.getZ() - 1);
		size = new BlockPos(1, 3, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(-1 + globalOffset.getX(), globalOffset.getY() + 5, globalOffset.getZ() - 1);
		size = new BlockPos(1, 2, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(globalOffset.getX(), globalOffset.getY() + 6, globalOffset.getZ() - 1);
		size = new BlockPos(1, 3, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(1 + globalOffset.getX(), globalOffset.getY() + 5, globalOffset.getZ() - 1);
		size = new BlockPos(1, 2, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(2 + globalOffset.getX(), globalOffset.getY() + 3, globalOffset.getZ() - 1);
		size = new BlockPos(1, 3, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(3 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ() - 1);
		size = new BlockPos(1, 4, 2);
		structure.add(new ChildStructure(offset, size, OBSIDIAN));

		offset = new BlockPos(globalOffset.getX() - 2, globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(5, 1, 1);
		BlockState state = SANDSTONE_STAIRS.defaultBlockState().setValue(FACING, SOUTH);
		structure.add(new ChildStructure(offset, size, SANDSTONE_STAIRS, state));
	}

	/**
	 * Add oak fenced doorway entry turning front (oriented along the x axis).
	 * 
	 * @param structure    structure where door is added to.
	 * @param globalOffset global offset.
	 */
	public static void addOakFencedDoorEntryFront(Structure structure, BlockPos globalOffset) {
		BlockPos offset = new BlockPos(-1 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		BlockPos size = new BlockPos(2, 3, 1);
		structure.add(createAirStructure(offset, size));
		offset = new BlockPos(-2 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(1, 3, 1);
		structure.add(createOakFenceStructure(offset, size));
		offset = new BlockPos(1 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(1, 3, 1);
		structure.add(createOakFenceStructure(offset, size));
		offset = new BlockPos(-2 + globalOffset.getX(), 3 + globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(4, 1, 1);
		structure.add(createOakFenceStructure(offset, size));
	}

	/**
	 * Add doorway entry at turning sideway (oriented along the Z axis).
	 * 
	 * @param structure    structure where door is added to.
	 * @param globalOffset global offset.
	 */
	public static void addOakFencedDoorEntryFrontSideways(Structure structure, BlockPos globalOffset) {
		BlockPos offset = new BlockPos(globalOffset.getX(), globalOffset.getY(), globalOffset.getZ() + 1);
		BlockPos size = new BlockPos(1, 3, 2);
		structure.add(createAirStructure(offset, size));

		offset = new BlockPos(globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(1, 3, 1);
		structure.add(createOakFenceStructure(offset, size));

		offset = new BlockPos(globalOffset.getX(), globalOffset.getY(), globalOffset.getZ() + 3);
		size = new BlockPos(1, 3, 1);
		structure.add(createOakFenceStructure(offset, size));

		offset = new BlockPos(globalOffset.getX(), 3 + globalOffset.getY(), globalOffset.getZ());
		size = new BlockPos(1, 1, 4);
		structure.add(createOakFenceStructure(offset, size));
	}

	/**
	 * Add stair up.
	 * 
	 * @param height       stairs height
	 * @param structure    structure where stair is added to.
	 * @param globalOffset global offset.
	 */
	public static void addSolidStairUp(int height, StairsMaterial materials, Structure structure,
			BlockPos globalOffset) {
		addSolidStairsUp(height, materials, structure, structure, globalOffset);
	}

	/**
	 * Add solid stairs up.
	 * 
	 * @param height        stairs height
	 * 
	 * @param structure     structure where structure is added to.
	 * @param postStructure structure to add structure for post processing.
	 * @param globalOffset  global offset.
	 */
	public static void addSolidStairsUp(int height, StairsMaterial materials, Structure structure,
			Structure postStructure, BlockPos globalOffset) {

		int xOffset = globalOffset.getX();

		for (int index = 0; index < height; index++) {

			// add stair block
			int zOffset = globalOffset.getZ() + index;
			int yOffset = globalOffset.getY() + 1 + index;
			BlockPos offset = new BlockPos(xOffset, yOffset, zOffset);
			BlockPos size = new BlockPos(2, 1, 1);
			structure.add(new ChildStructure(offset, size, materials.getStairMaterial(), materials.getState()));

			// add solid block
			if (index > 0) {
				yOffset = globalOffset.getY() + 1;
				int ySize = index;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(2, ySize, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));
			}

			// add air block
			yOffset = globalOffset.getY() + 2 + index;
			offset = new BlockPos(xOffset, yOffset, zOffset);
			size = new BlockPos(2, 4, 1);
			postStructure.add(new ChildStructure(offset, size, AIR));
		}
	}

	/**
	 * Add spiral stairs up.
	 * 
	 * @param height        stairs height
	 * 
	 * @param structure     structure where structure is added to.
	 * @param postStructure structure to add structure for post processing.
	 * @param globalOffset  global offset.
	 */
	public static void addSpiralStairsUp(int height, StairsMaterial materials, Structure structure,
			Structure postStructure, BlockPos globalOffset) {

		for (int index = 0; index < height; index++) {

			if (index % 4 == 0) {

				int xOffset = globalOffset.getX() + 1;
				int yOffset = globalOffset.getY() + 1 + index;
				int zOffset = globalOffset.getZ();
				BlockPos offset = new BlockPos(xOffset, yOffset, zOffset);
				BlockPos size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				xOffset = globalOffset.getX() + 1;
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() + 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				BlockState state = materials.getState().setValue(FACING, WEST);
				structure.add(new ChildStructure(offset, size, materials.getStairMaterial(), state));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));

				xOffset = globalOffset.getX();
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() + 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));
			}

			if (index % 4 == 1) {

				int xOffset = globalOffset.getX() + 1;
				int yOffset = globalOffset.getY() + 1 + index;
				int zOffset = globalOffset.getZ();
				BlockPos offset = new BlockPos(xOffset, yOffset, zOffset);
				BlockPos size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				xOffset = globalOffset.getX();
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ();
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				BlockState state = materials.getState().setValue(FACING, NORTH);						
				structure.add(new ChildStructure(offset, size, materials.getStairMaterial(), state));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));

				xOffset = globalOffset.getX();
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() - 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));

			}

			if (index % 4 == 2) {

				int xOffset = globalOffset.getX() + 1;
				int zOffset = globalOffset.getZ();
				int yOffset = globalOffset.getY() + 1 + index;
				BlockPos offset = new BlockPos(xOffset, yOffset, zOffset);
				BlockPos size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				xOffset = globalOffset.getX() + 1;
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() - 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				BlockState state = materials.getState().setValue(FACING, EAST);
				structure.add(new ChildStructure(offset, size, materials.getStairMaterial(), state));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));

				xOffset = globalOffset.getX() + 2;
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() - 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));
			}

			if (index % 4 == 3) {

				int xOffset = globalOffset.getX() + 1;
				int zOffset = globalOffset.getZ();
				int yOffset = globalOffset.getY() + 1 + index;
				BlockPos offset = new BlockPos(xOffset, yOffset, zOffset);
				BlockPos size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				xOffset = globalOffset.getX() + 2;
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ();
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				BlockState state = materials.getState().setValue(FACING, SOUTH);
				structure.add(new ChildStructure(offset, size, materials.getStairMaterial(), state));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));

				xOffset = globalOffset.getX() + 2;
				yOffset = globalOffset.getY() + 1 + index;
				zOffset = globalOffset.getZ() + 1;
				offset = new BlockPos(xOffset, yOffset, zOffset);
				size = new BlockPos(1, 1, 1);
				structure.add(new ChildStructure(offset, size, materials.getSolidMaterial()));

				// add air block
				offset = offset.offset(0, 1, 0);
				size = new BlockPos(1, 3, 1);
				postStructure.add(new ChildStructure(offset, size, AIR));
			}
		}
	}

	/**
	 * Add mob spawner.
	 * 
	 * @param structure    structure where mob spawner is added to.
	 * @param globalOffset global offset.
	 */
	public static void addMobSpawner(Structure structure, BlockPos globalOffset) {
		BlockPos size = new BlockPos(1, 1, 1);
		structure.add(new ChildStructure(globalOffset, size, SPAWNER));
	}

	/**
	 * Select random floor material.
	 * 
	 * @return random floor material
	 */
	public static BuildMaterial selectFloorMaterial(Random random) {

		int selection = random.nextInt(17);

		switch (selection) {

		case 1:
			return createBuildMaterial(BRICKS);
		case 2:
			return createBuildMaterial(NETHER_BRICKS);
		case 3:
			return createBuildMaterial(SANDSTONE);
		case 4:
			return createBuildMaterial(STONE);
		case 5:
			return createBuildMaterial(ANDESITE);
		case 6:
			return createBuildMaterial(DIORITE);
		case 7:
			return createBuildMaterial(POLISHED_GRANITE);
		case 8:
			return createBuildMaterial(STONE_BRICKS);
		case 9:
			return createBuildMaterial(CHISELED_STONE_BRICKS);
		case 10:
			return createBuildMaterial(CRACKED_STONE_BRICKS);
		case 11:
			return createBuildMaterial(MOSSY_STONE_BRICKS);
		case 12:
			return createBuildMaterial(MOSSY_COBBLESTONE);
		case 13:
			return createBuildMaterial(COBBLESTONE);
		case 14:
			return createBuildMaterial(MAGMA_BLOCK);
		case 15:
			return createBuildMaterial(OAK_LOG);
		case 16:
			return createBuildMaterial(SPRUCE_LOG);
		case 17:
			return createBuildMaterial(OAK_PLANKS);

		default:
			return createBuildMaterial(SANDSTONE);
		}
	}

	/**
	 * Select random wall material.
	 * 
	 * @return random wall material
	 */
	public static BuildMaterial selectWallMaterial(Random random) {
		int selection = random.nextInt(8);

		switch (selection) {

		case 0:
			return createBuildMaterial(SANDSTONE);
		case 1:
			return createBuildMaterial(CHISELED_RED_SANDSTONE);
		case 2:
			return createBuildMaterial(CHISELED_SANDSTONE);
		case 3:
			return createBuildMaterial(CUT_RED_SANDSTONE);
		case 4:
			return createBuildMaterial(CUT_SANDSTONE);
		case 5:
			return createBuildMaterial(RED_SANDSTONE);
		case 6:
			return createBuildMaterial(SMOOTH_RED_SANDSTONE);
		case 7:
			return createBuildMaterial(SMOOTH_SANDSTONE);

		default:
			return createBuildMaterial(SANDSTONE);
		}
	}

	/**
	 * Select random window material.
	 * 
	 * @return random window material
	 */
	public static BuildMaterial selectWindowMaterial(Random random) {
		int selection = random.nextInt(13);

		switch (selection) {

		case 0:
			return createBuildMaterial(WHITE_STAINED_GLASS_PANE);
		case 1:
			return createBuildMaterial(BLUE_STAINED_GLASS_PANE);
		case 2:
			return createBuildMaterial(CYAN_STAINED_GLASS_PANE);
		case 3:
			return createBuildMaterial(GREEN_STAINED_GLASS_PANE);
		case 4:
			return createBuildMaterial(LIGHT_BLUE_STAINED_GLASS_PANE);
		case 5:
			return createBuildMaterial(LIME_STAINED_GLASS_PANE);
		case 6:
			return createBuildMaterial(MAGENTA_STAINED_GLASS_PANE);
		case 7:
			return createBuildMaterial(ORANGE_STAINED_GLASS_PANE);
		case 8:
			return createBuildMaterial(PINK_STAINED_GLASS_PANE);
		case 9:
			return createBuildMaterial(PURPLE_STAINED_GLASS_PANE);
		case 10:
			return createBuildMaterial(RED_STAINED_GLASS_PANE);
		case 11:
			return createBuildMaterial(LIGHT_GRAY_STAINED_GLASS_PANE);
		case 12:
			return createBuildMaterial(YELLOW_STAINED_GLASS_PANE);

		default:
			return createBuildMaterial(GLASS_PANE);
		}
	}

	/**
	 * Stairs material factory method.
	 * 
	 * @param state         stair material state.
	 * @param stairMaterial stair material.
	 * @param solidMaterial solid material.
	 */
	public static StairsMaterial createInstance(BlockState state, Block stairMaterial, Block solidMaterial) {
		return new StairsMaterial(state, stairMaterial, solidMaterial);
	}

	/**
	 * Build material factory method.
	 * 
	 * @param solidMaterial build material.
	 */
	public static BuildMaterial createBuildMaterial(Block buildMaterial) {
		return new BuildMaterial(buildMaterial, buildMaterial.defaultBlockState());
	}

}
