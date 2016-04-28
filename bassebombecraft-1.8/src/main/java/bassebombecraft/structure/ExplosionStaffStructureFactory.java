package bassebombecraft.structure;

import java.util.Random;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ExplosionStaffStructureFactory implements StructureFactory {

	private static final int MAX_RIFT_SEGMENTS = 40;
	static final int X_SIZE = 1;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 3;
	static final int Z_OFFSET = 4;
	static final int Y_OFFSET_DOWN = -2;
	static Random random = new Random();
	static Structure spike = createPileOfTnt();
	
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {		
		if (isGroundBlock) return createRiftOfTnt(); // create new instance due to random displacement			
		return spike;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}
	
	static Structure createRiftOfTnt() {		
		CompositeStructure composite = new CompositeStructure();
		int displacement = 0;			
		int riftSegments = random.nextInt(MAX_RIFT_SEGMENTS+1);
		for(int index=0; index<riftSegments; index++) {
			displacement = calculateDisplacement(index, displacement);			
					
			BlockDirective offset = new BlockDirective(displacement, Y_OFFSET_DOWN, Z_OFFSET+(Z_SIZE)*index); 
			BlockDirective size = new BlockDirective(X_SIZE,Y_SIZE,Z_SIZE);
			composite.add(new ChildStructure(offset, size, Blocks.tnt));						

			if(index==0) {

				offset = new BlockDirective(offset.getX(), offset.getY()+1, offset.getZ());
				size = new BlockDirective(1, 1, 1);
				composite.add(new ChildStructure(offset, size, Blocks.redstone_block));						
			}
		}
				
		return composite;
	}

	static Structure createPileOfTnt() {
		CompositeStructure composite = new CompositeStructure();
		
		BlockDirective offset = new BlockDirective(0,0,0);
		BlockDirective size = new BlockDirective(1, 5, 1);
		composite.add(new ChildStructure(offset, size, Blocks.tnt));

		offset = new BlockDirective(-1,0,0);
		size = new BlockDirective(1, 3, 1);
		composite.add(new ChildStructure(offset, size, Blocks.tnt));

		offset = new BlockDirective(0,0,1);
		size = new BlockDirective(1, 7, 1);
		composite.add(new ChildStructure(offset, size, Blocks.tnt));
				
		offset = new BlockDirective(1,0,0);
		size = new BlockDirective(1, 2, 1);
		composite.add(new ChildStructure(offset, size, Blocks.redstone_block));		
		
		return composite;
	}

	static int calculateDisplacement(int index, int displacement) {
		if(index == 0) return 0;
		int displacementRandom = random.nextInt(3);
		if(displacementRandom == 1 ) return displacement+1;
		if(displacementRandom == 2 ) return displacement-1;
		return displacement;
	}	
	
}

