package bassebombecraft.structure.debug;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import bassebombecraft.structure.StructureFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class DebuggingStructureFactory implements StructureFactory {

	static final int DEBUGGING_Y_DISPLACEMENT = 20;
	static final Block DEBUGGING_BLOCK_TYPE = Blocks.ICE;
	StructureFactory factory;
		
	/**
	 * DebuggingStructureFactory constructor.
	 * 
	 * @param factory factory whose output is debugged.
	 */
	public DebuggingStructureFactory(StructureFactory factory) {
		super();
		this.factory = factory;
	}

	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery) {		
		Structure srcStructure = factory.getInstance(isGroundBlock, sourceBlock, worldQuery);		
		CompositeStructure composite = new CompositeStructure();
		composite.add(srcStructure);
		composite.add(createDebuggingStructure(srcStructure));
		return composite;
	}

	@Override
	public boolean calculateOffsetFromPlayerFeet() {
		return true;
	}
	
	Structure createDebuggingStructure(Structure srcStructure) {		
		if(srcStructure.isComposite()) {			
			CompositeStructure composite = new CompositeStructure();
			Structure[] srcChildren = srcStructure.getChildren();
			for(Structure srcChild : srcChildren) {
				composite.add(createDebuggingStructure(srcChild));
			}
			return composite;			
		}		
		return createDebuggingChildStructure(srcStructure);
	}

	Structure createDebuggingChildStructure(Structure srcStructure) {
		BlockDirective offset = new BlockDirective(
				srcStructure.getOffsetX(),
				srcStructure.getOffsetY()+DEBUGGING_Y_DISPLACEMENT,
				srcStructure.getOffsetZ(),
				DEBUGGING_BLOCK_TYPE);
		BlockDirective size = new BlockDirective(
				srcStructure.getSizeX(),
				srcStructure.getSizeY(),
				srcStructure.getSizeZ(), 
				DEBUGGING_BLOCK_TYPE );
		return new ChildStructure(offset, size, DEBUGGING_BLOCK_TYPE);
	}
			
}

