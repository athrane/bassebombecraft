package bassebombecraft.structure;

import java.util.ArrayList;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Composite structure which consist of multiple structures.
 */
public class CompositeStructure implements Structure {

	ArrayList<Structure> children = new ArrayList<Structure>();

	@Override
	public int getSizeX() {
		return 0;
	}

	@Override
	public int getSizeY() {
		return 0;
	}

	@Override
	public int getSizeZ() {
		return 0;
	}

	@Override
	public int getOffsetX() {
		return 0;
	}

	@Override
	public int getOffsetZ() {
		return 0;
	}

	@Override
	public int getOffsetY() {
		return 0;
	}

	@Override
	public Block getBlock() {
		throw new UnsupportedOperationException("Getting block not supported for composite structure.");
	}

	@Override
	public BlockState getBlockState() {
		throw new UnsupportedOperationException("Getting block state not supported for composite structure.");
	}

	@Override
	public boolean isComposite() {
		return true;
	}

	@Override
	public Structure[] getChildren() {
		return children.toArray(new Structure[children.size()]);
	}

	@Override
	public void add(Structure child) {
		this.children.add(child);
	}

	/**
	 * Factory method for composite structure.
	 * 
	 * @return composite structure
	 */
	public static CompositeStructure getInstance() {
		return new CompositeStructure();
	}

}
