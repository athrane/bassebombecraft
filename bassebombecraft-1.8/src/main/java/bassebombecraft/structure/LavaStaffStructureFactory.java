package bassebombecraft.structure;

import java.util.Random;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class LavaStaffStructureFactory implements StructureFactory {

	private static final int RIFT_SEGMENTS = 10;
	static final int X_SIZE = 1;
	static final int Y_SIZE = 2;
	static final int Z_SIZE = 2;
	static final int Y_OFFSET_DOWN = -1;
	static Random random = new Random();
	static Structure spike = createSpikeOfLava();
	
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {		
		if (isGroundBlock) return createRiftOfLava(); // create new instance due to random displacement			
		return spike;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}
	
	static Structure createRiftOfLava() {		
		CompositeStructure composite = new CompositeStructure();
		int displacement = 0;			
		
		for(int index=0; index<RIFT_SEGMENTS; index++) {
			displacement = calculateDisplacement(index, displacement);			
					
			BlockDirective offset = new BlockDirective(displacement, Y_OFFSET_DOWN, (Z_SIZE)*index); 
			BlockDirective size = new BlockDirective(X_SIZE,Y_SIZE,Z_SIZE);
			composite.add(new ChildStructure(offset, size, Blocks.LAVA));						
		}
 				
		return composite;
	}

	static Structure createSpikeOfLava() {		
		BlockDirective offset = new BlockDirective(0,0,0);
		BlockDirective size = new BlockDirective(1, 5, 1);
		return new ChildStructure(offset, size, Blocks.LAVA);
	}

	static int calculateDisplacement(int index, int displacement) {
		if(index == 0) return 0;
		int displacementRandom = random.nextInt(3);
		if(displacementRandom == 1 ) return displacement+1;
		if(displacementRandom == 2 ) return displacement-1;
		return displacement;
	}	
	
}

