package bassebombecraft.structure;

import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Structure} interface which implements
 * a child structure.
 */
public class ChildStructure implements Structure {

	final static Structure[] EMPTY_CHILDREN = new Structure[] {};
	BlockPos offset;
	BlockPos size;
	Block block;
	IBlockState state;	

	/**
	 * ChildStructure constructor which builds a structure out of air.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 */
	public ChildStructure(BlockPos offset, BlockPos size) {
		this(offset, size, Blocks.AIR);
	}
	
	/**
	 * ChildStructure constructor which builds a structure out of air.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 */
	@Deprecated
	public ChildStructure(BlockDirective offset, BlockDirective size) {
		this(offset, size, Blocks.AIR);
	}

	/**
	 * ChildStructure constructor.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 * @param block block type used to build structure.
	 */
	@Deprecated
	public ChildStructure(BlockDirective offset, BlockDirective size, Block block) {
		super();
		this.offset = new BlockPos(offset.getX(), offset.getY(), offset.getZ());
		this.size= new BlockPos(size.getX(), size.getY(), size.getZ());
		this.block = block;
	}

	/**
	 * ChildStructure constructor.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 * @param block block type used to build structure.
	 */
	public ChildStructure(BlockPos offset, BlockPos size, Block block) {
		super();
		this.offset = offset;
		this.size = size;
		this.block = block;
	}
	
	/**
	 * ChildStructure constructor.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 * @param block block type used to build structure.
	 * @param state block state used to build structure.
	 */
	@Deprecated
	public ChildStructure(BlockDirective offset, BlockDirective size, Block block, IBlockState state) {
		super();
		this.offset = new BlockPos(offset.getX(), offset.getY(), offset.getZ());
		this.size= new BlockPos(size.getX(), size.getY(), size.getZ());
		this.block = block;
		this.state = state;
	}

	/**
	 * ChildStructure constructor.
	 * 
	 * @param offset player offset.
	 * @param size structure size.
	 * @param block block type used to build structure.
	 * @param state block state used to build structure.
	 */
	public ChildStructure(BlockPos offset, BlockPos size, Block block, IBlockState state) {
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
	public IBlockState getBlockState() {
		if(state != null) return state;
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
	
	public static Structure createAirStructure(BlockDirective offset, BlockDirective size) {
		return new ChildStructure(offset, size);
	}

	public static Structure createAirStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size);
	}
	
	public static Structure createWaterStructure(BlockDirective offset, BlockDirective size) {
		return new ChildStructure(offset, size, Blocks.WATER);
	}
	
	public static Structure createWaterStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.WATER);
	}
	
	public static Structure createIceStructure(BlockDirective offset, BlockDirective size) {
		return new ChildStructure(offset, size, Blocks.ICE);
	}	

	public static Structure createIceStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.ICE);
	}	
	
	public static Structure createWoodStructure(BlockDirective offset, BlockDirective size) {
		return new ChildStructure(offset, size, Blocks.PLANKS);
	}	

	public static Structure createOakFenceStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.OAK_FENCE);
	}	

	@Deprecated
	public static Structure createTorchStructure(BlockDirective offset, BlockDirective size) {
		return new ChildStructure(offset, size, Blocks.TORCH);
	}	

	public static Structure createTorchStructure(BlockPos offset, BlockPos size) {
		return new ChildStructure(offset, size, Blocks.TORCH);
	}
	
}
