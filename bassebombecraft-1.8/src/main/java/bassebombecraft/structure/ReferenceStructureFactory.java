package bassebombecraft.structure;

import java.util.Random;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class ReferenceStructureFactory implements StructureFactory {

	static Random random = new Random();
	static Structure aboveGroundRoom = createAboveGroundRoom();
	static Structure groundRoom = createGroundRoom();
	
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {
		
		// create ground blocks
		if (isGroundBlock) {
			return groundRoom;
		} 		
		return aboveGroundRoom;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}

	static Structure createAboveGroundRoom() {		
		BlockPos offset = new BlockPos(0,0,0); 
		BlockPos size = new BlockPos(2,4,6);			
		return new ChildStructure(offset, size, Blocks.COBBLESTONE);
	}
	
	static Structure createGroundRoom() {		
		BlockDirective offset = new BlockDirective(0,0,0);
		BlockDirective size = new BlockDirective(2, 4, 6);
		return new ChildStructure(offset, size, Blocks.ICE);
	}
			
}

