package bassebombecraft.structure;

import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createWaterStructure;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;

public class DeepHoleStaffStructureFactory implements StructureFactory{

	private static final int HOLE_WIDTH_AND_DEPTH = 5;
	private static final int WATER_HEIGHT = 2;
	private static final int HOLE_HEIGHT =50;
	static Structure shaft = createShaftOfAir();
	
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {
		
		// create ground blocks
		if (isGroundBlock) {
			return shaft;
		} 
		return shaft;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}

	static Structure createShaftOfAir() {		
		CompositeStructure composite = new CompositeStructure();
		
		BlockDirective offset = new BlockDirective(0,-HOLE_HEIGHT,0);
		BlockDirective size = new BlockDirective(HOLE_WIDTH_AND_DEPTH, HOLE_HEIGHT, HOLE_WIDTH_AND_DEPTH);
		composite.add(createAirStructure(offset, size));			

		offset = new BlockDirective(1,-HOLE_HEIGHT+10,HOLE_WIDTH_AND_DEPTH);
		size = new BlockDirective(HOLE_WIDTH_AND_DEPTH-2, HOLE_HEIGHT-10, 1);
		composite.add(createAirStructure(offset, size));			
		
		offset = new BlockDirective(5,-HOLE_HEIGHT+10,1);
		size = new BlockDirective(1, HOLE_HEIGHT-10, HOLE_WIDTH_AND_DEPTH-2);
		composite.add(createAirStructure(offset, size));			

		offset = new BlockDirective(-1,-HOLE_HEIGHT+10,1);
		size = new BlockDirective(1, HOLE_HEIGHT-10, HOLE_WIDTH_AND_DEPTH-2);
		composite.add(createAirStructure(offset, size));			
		
		
		offset = new BlockDirective(0,-(HOLE_HEIGHT+WATER_HEIGHT),0);
		size = new BlockDirective(HOLE_WIDTH_AND_DEPTH, WATER_HEIGHT, HOLE_WIDTH_AND_DEPTH);
		composite.add(createWaterStructure(offset, size));			

		offset = new BlockDirective(HOLE_WIDTH_AND_DEPTH,-5,0);
		size = new BlockDirective(1,1,1);
		composite.add(createWaterStructure(offset, size));			
				
		return composite;
	}
			
}

