package bassebombecraft.structure;

import java.util.Random;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class GardenStructureFactory implements StructureFactory {

	static Random random = new Random();
	static Structure aboveGroundRoom = createAboveGroundRoom();
	static Structure enclosure = createEnclosure();
	static Structure flowerBed = createVegetables();

	
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {
		
		// create ground blocks
		if (isGroundBlock) {
			return createEnclosure(); // create new instance due to random displacement	
		} 		
		return flowerBed;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}
	
	static Structure createAboveGroundRoom() {		
		BlockDirective offset = new BlockDirective(0,0,0);
		BlockDirective size = new BlockDirective(2,1,6);
		return new ChildStructure(offset, size, Blocks.FARMLAND);
	}

	static Structure createEnclosure() {	
		CompositeStructure composite = new CompositeStructure();
		
		// left front
		BlockDirective offset = new BlockDirective(1,0,0);
		BlockDirective size = new BlockDirective(4, 2, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES2));			

		// right front
		offset = new BlockDirective(-5,0,0);
		size = new BlockDirective(4, 2, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES2));			

		// portal
		offset = new BlockDirective(1,2,0);
		size = new BlockDirective(1, 1, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES));			
		offset = new BlockDirective(-2,2,0);
		size = new BlockDirective(1, 1, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES));					
		offset = new BlockDirective(-1,3,0);
		size = new BlockDirective(2, 1, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES));			

		// left side
		offset = new BlockDirective(4,0,1);
		size = new BlockDirective(1, 2, 9);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES2));			

		// right side
		offset = new BlockDirective(-5,0,1);
		size = new BlockDirective(1, 2, 9);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES2));			
		
		// back
		offset = new BlockDirective(-5,0,10);
		size = new BlockDirective(10, 2, 1);
		composite.add(new ChildStructure(offset, size, Blocks.LEAVES2));			
										
		offset = new BlockDirective(-3,0,4);
		size = new BlockDirective(6, 1, 6);
		composite.add(new ChildStructure(offset, size, calculateFlower()));			

		// path
		offset = new BlockDirective(-1,-1,0);
		size = new BlockDirective(2, 1, 4);
		composite.add(new ChildStructure(offset, size, Blocks.SANDSTONE));			
		
		
		return composite;
	}

	static Block calculateFlower() {
		int flowerId = random.nextInt(2);	
		if(flowerId==0) return Blocks.RED_FLOWER;
		if(flowerId==1) return Blocks.YELLOW_FLOWER;			
		return Blocks.COCOA;
	}

	static Structure createVegetables() {	
		CompositeStructure composite = new CompositeStructure();
		
		// left part
		BlockDirective offset = new BlockDirective(-4,0,0);
		BlockDirective size = new BlockDirective(2, 1, 8);
		composite.add(new ChildStructure(offset, size, Blocks.FARMLAND));			

		offset = new BlockDirective(-4,0,0);
		size = new BlockDirective(2, 2, 8);
		composite.add(new ChildStructure(offset, size, Blocks.CARROTS));			

		// right part
		offset = new BlockDirective(0,0,0);
		size = new BlockDirective(2, 1, 8);
		composite.add(new ChildStructure(offset, size, Blocks.FARMLAND));			

		offset = new BlockDirective(0,0,0);
		size = new BlockDirective(2, 2, 8);
		composite.add(new ChildStructure(offset, size, Blocks.POTATOES));			
		
		// path
		offset = new BlockDirective(-2,-1,0);
		size = new BlockDirective(2, 1, 8);
		composite.add(new ChildStructure(offset, size, Blocks.SANDSTONE));			
		
		// fence
		offset = new BlockDirective(-5,0,0);
		size = new BlockDirective(1, 1, 9);
		composite.add(new ChildStructure(offset, size, Blocks.OAK_FENCE));			
		offset = new BlockDirective(2,0,0);
		size = new BlockDirective(1, 1, 9);
		composite.add(new ChildStructure(offset, size, Blocks.OAK_FENCE));			
		offset = new BlockDirective(-4,0,8);
		size = new BlockDirective(6, 1, 1);
		composite.add(new ChildStructure(offset, size, Blocks.OAK_FENCE));			
				
		return composite;
	}
	
}

