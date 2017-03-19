package bassebombecraft.item.action.build;

import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createOakFenceStructure;

import bassebombecraft.item.action.build.tower.StairsMaterial;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * Utility class for build block based structures.
 */
public class BuildUtils {

	/**
	 * Add oak fenced doorway entry turning front.
	 * 
	 * @param structure
	 *            structure where door is added to.
	 * @param globalOffset
	 *            global offset.
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
	 * Add stair up.
	 * 
	 * @param height
	 *            stairs height
	 * @param structure
	 *            structure where stair is added to.
	 * @param globalOffset
	 *            global offset.
	 */
	public static void addSolidStairUp(int height, StairsMaterial materials, Structure structure,
			BlockPos globalOffset) {
		addSolidStairUp(height, materials, structure, structure, globalOffset);
	}

	/**
	 * Add stair up.
	 * 
	 * @param height
	 *            stairs height
	 * 
	 * @param structure
	 *            structure where structure is added to.
	 * @param postStructure
	 *            structure to add structure for post processing.
	 * @param globalOffset
	 *            global offset.
	 */
	public static void addSolidStairUp(int height, StairsMaterial materials, Structure structure,
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
			postStructure.add(new ChildStructure(offset, size, Blocks.AIR));
		}
	}

	/**
	 * StairsMaterial factory method.
	 * 
	 * @param state
	 *            stair material state.
	 * @param stairMaterial
	 *            stair material.
	 * @param solidMaterial
	 *            solid material.
	 */
	public static StairsMaterial createInstance(IBlockState state, Block stairMaterial, Block solidMaterial) {
		return new StairsMaterial(state, stairMaterial, solidMaterial);
	}
}
