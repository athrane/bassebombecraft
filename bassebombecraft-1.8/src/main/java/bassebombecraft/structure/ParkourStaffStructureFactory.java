package bassebombecraft.structure;

import static bassebombecraft.ModConstants.UNITY_BLOCK_SIZE;

import java.util.Random;

import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class ParkourStaffStructureFactory implements StructureFactory {

	static final Block BLOCK_TYPE = Blocks.pumpkin;
	static Random random = new Random();
	private BlockPos offset;

	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {
		offset = new BlockPos(0, 0, 0);
		CompositeStructure composite = new CompositeStructure();

		for (int counter = 0; counter < 100; counter++) {
			int randomId = random.nextInt(4);
			composite.add(addStructure(randomId));
		}

		return composite;

	}

	/**
	 * Select the structure to add.
	 * 
	 * @param offset
	 *            structure offset.
	 * @param randomId
	 *            random ID use to select the structure
	 * @return selected structure.
	 */
	Structure addStructure(int randomId) {
		if (randomId == 0) return createBlock0x0x3();
		if (randomId == 1) return createBlock0x1x2();
		if (randomId == 2) return createBlock1x1x2();
		if (randomId == 3) return createBlockm1x1x2();
		return createPlateau();
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}

	void updateOffsetWithDelta(int x, int y, int z) {
		offset = offset.add(x, y, z);
	}
	
	Structure createPlateau() {
		final int plateauSize = 2;		
		final int plateau_height = 1;		
		BlockPos targetOffset = offset.add(0,0,1);
		BlockPos size = new BlockPos(plateauSize,plateau_height,plateauSize);
		updateOffsetWithDelta(0,0,plateauSize);
		return new ChildStructure(targetOffset, size, Blocks.wool);
	}
	
	Structure createBlock0x0x3() {		
		BlockPos targetOffset = offset.add(0,0,4);
		updateOffsetWithDelta(0,0,4);		
		return new ChildStructure(targetOffset, UNITY_BLOCK_SIZE, BLOCK_TYPE);
	}

	Structure createBlock0x1x2() {
		BlockPos targetOffset = offset.add(0,1,3);
		updateOffsetWithDelta(0,1,3);				
		return new ChildStructure(targetOffset, UNITY_BLOCK_SIZE, BLOCK_TYPE);
	}

	Structure createBlock1x1x2() {
		BlockPos targetOffset = offset.add(1,1,3);
		updateOffsetWithDelta(1,1,3);				
		return new ChildStructure(targetOffset, UNITY_BLOCK_SIZE, BLOCK_TYPE);
	}

	Structure createBlockm1x1x2() {
		BlockPos targetOffset = offset.add(-1,1,3);
		updateOffsetWithDelta(-1,1,3);						
		return new ChildStructure(targetOffset, UNITY_BLOCK_SIZE, BLOCK_TYPE);
	}

}
