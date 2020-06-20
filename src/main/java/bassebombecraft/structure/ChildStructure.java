package bassebombecraft.structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Structure} interface which implements a
 * child structure.
 */
public class ChildStructure implements Structure {

	static final Structure[] EMPTY_CHILDREN = new Structure[] {};
	BlockPos offset;
	BlockPos size;
	Block block;
	BlockState state;

	/**
	 * Constructor which builds a structure out of air.
	 * 
	 * @param offset player offset.
	 * @param size   structure size.
	 */
	public ChildStructure(BlockPos offset, BlockPos size) {
		this(offset, size, Blocks.AIR);
	}

	/**
	 * Constructor.
	 * 
	 * @param offset player offset.
	 * @param size   structure size.
	 * @param block  block type used to build structure.
	 */
	public ChildStructure(BlockPos offset, BlockPos size, Block block) {
		super();
		this.offset = offset;
		this.size = size;
		this.block = block;
	}

	/**
	 * Constructor.
	 * 
	 * @param offset player offset.
	 * @param size   structure size.
	 * @param block  block type used to build structure.
	 * @param state  block state used to build structure.
	 */
	public ChildStructure(BlockPos offset, BlockPos size, Block block, BlockState state) {
		super();
		this.offset = offset;
		this.size = size;
		this.block = block;
		this.state = state;
	}

	@Override
	public int getSizeX() {
		return size.getX();
	}

	@Override
	public int getSizeY() {
		return size.getY();
	}

	@Override
	public int getSizeZ() {
		return size.getZ();
	}

	@Override
	public int getOffsetX() {
		return offset.getX();
	}

	@Override
	public int getOffsetY() {
		return offset.getY();
	}

	@Override
	public int getOffsetZ() {
		return offset.getZ();
	}

	@Override
	public Block getBlock() {
		return block;
	}

	@Override
	public BlockState getBlockState() {
		if (state != null)
			return state;
		return block.getDefaultState();
	}

	@Override
	final public boolean isComposite() {
		return false;
	}

	@Override
	final public Structure[] getChildren() {
		return EMPTY_CHILDREN;
	}

	@Override
	public void add(Structure child) {
		throw new UnsupportedOperationException("Add child structure is not supported for child structure.");
	}

	public static Structure createAirStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size);
	}

	public static Structure createWaterStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.WATER);
	}

	public static Structure createIceStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.ICE);
	}

	public static Structure createWoodStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.OAK_PLANKS);
	}

	public static Structure createOakFenceStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.OAK_FENCE);
	}

	public static Structure createTorchStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.TORCH);
	}

}
