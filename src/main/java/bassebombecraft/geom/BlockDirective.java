package bassebombecraft.geom;

import static bassebombecraft.block.BlockUtils.isTypeBlockDirective;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Class for directing the creation/modification/harvesting of blocks.
 */
public class BlockDirective {

	/**
	 * Block to process.
	 */
	Block block;

	/**
	 * Defines whether block should be harvested when processed.
	 */
	boolean harvest;

	/**
	 * Position of block to be harvested.
	 */
	BlockPos blockPos;

	/**
	 * Block state.
	 */
	BlockState state;

	/**
	 * Player which perform any harvesting of the block.
	 */
	Optional<PlayerEntity> optPlayer;

	/**
	 * BlockDirective constructor. Block is located at (0,0,0). Block is made of air
	 * and can be harvested.
	 */
	public BlockDirective() {
		set(0, 0, 0);
		this.block = Blocks.AIR;
		this.harvest = true;
	}

	/**
	 * BlockDirective constructor. Block is made of air and can be harvested.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Deprecated
	public BlockDirective(int x, int y, int z) {
		set(x, y, z);
		this.block = Blocks.AIR;
		this.harvest = true;
	}

	/**
	 * BlockDirective constructor. Block is a copy of source block directive.
	 * 
	 * @param other source block directive.
	 */
	public BlockDirective(BlockDirective other) {
		set(other.getBlockPosition());
		this.block = other.block;
		this.harvest = other.harvestBlock();
		this.state = other.getState();
	}

	/**
	 * BlockDirective constructor.
	 * 
	 * @param blockPos block position
	 * @param block
	 * @param harvest  defines if block should be harvested.
	 */
	public BlockDirective(BlockPos blockPos, Block block, boolean harvest) {
		this.blockPos = blockPos;
		this.block = block;
		this.harvest = harvest;
	}

	/**
	 * BlockDirective constructor.
	 * 
	 * @param blockPos block position
	 * @param block
	 * @param harvest  defines if block should be harvested.
	 * @param player   player which should do the harvesting.
	 */
	BlockDirective(BlockPos blockPos, Block block, boolean harvest, PlayerEntity player) {
		this.blockPos = blockPos;
		this.block = block;
		this.harvest = harvest;
		this.optPlayer = ofNullable(player);
	}

	/**
	 * BlockDirective constructor.
	 * 
	 * Block is defined to be harvested.
	 * 
	 * @param blockPos block position
	 * @param block    block type.
	 */
	public BlockDirective(BlockPos blockPos, Block block) {
		this(blockPos, block, true);
	}

	/**
	 * Get X coordinate of position.
	 * 
	 * @return X coordinate of position.
	 */
	public int getX() {
		return blockPos.getX();
	}

	/**
	 * Get Y coordinate of position.
	 * 
	 * @return Y coordinate of position.
	 */
	public int getY() {
		return blockPos.getY();
	}

	/**
	 * Get Z coordinate of position.
	 * 
	 * @return Z coordinate of position.
	 */
	public int getZ() {
		return blockPos.getZ();
	}

	/**
	 * Set position.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	void set(int x, int y, int z) {
		blockPos = new BlockPos(x, y, z);
	}

	/**
	 * Set position.
	 * 
	 * @param position new position to set.
	 */
	void set(BlockPos position) {
		this.blockPos = new BlockPos(position);
	}

	public int hashCode() {
		return (blockPos.getX() + (blockPos.getY() << 8) + (blockPos.getZ() << 16));
	}

	public boolean equals(Object other) {
		if (isTypeBlockDirective(other)) {
			return equals((BlockDirective) other);
		}
		return false;
	}

	public boolean equals(BlockDirective other) {
		return blockPos.equals(other.getBlockPosition());
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Block getBlock() {
		return this.block;
	}

	/**
	 * returns true if block should harvested.
	 * 
	 * The block is harvested if the target block type is air and the block directs
	 * harvesting.
	 * 
	 * @return true if block should harvested.
	 */
	public boolean harvestBlock() {
		if (getBlock() != Blocks.AIR)
			return false;
		return this.harvest;
	}

	/**
	 * Return player.
	 * 
	 * @return player. Is returned as {@linkplain Optional} is it will be undefined
	 *         in many cases.
	 */
	public Optional<PlayerEntity> getPlayer() {
		return optPlayer;
	}

	/**
	 * Return block position.
	 * 
	 * @return block position.
	 */
	public BlockPos getBlockPosition() {
		return blockPos;
	}

	/**
	 * Return true if state is state is defined for this directive.
	 * 
	 * @return true if state is state is defined for this directive.
	 */
	public boolean isStateful() {
		return (this.state != null);
	}

	/**
	 * Returns defined block state. If state isn't defined then the default state
	 * for the block is returned.
	 * 
	 * @return defined block state. If state isn't defined then the default state
	 *         for the block is returned.
	 */
	public BlockState getState() {
		if (isStateful())
			return state;
		return block.getDefaultState();
	}

	/**
	 * Set state.
	 * 
	 * @param state new state.
	 */
	public void setState(BlockState state) {
		this.state = state;
	}

	/**
	 * Translates position of directive by subtraction of the translation vector.
	 * 
	 * @param translationVector translation vector
	 */
	public void translate(BlockPos translationVector) {
		int x = blockPos.getX() - translationVector.getX();
		int y = blockPos.getY() - translationVector.getY();
		int z = blockPos.getZ() - translationVector.getZ();
		set(x, y, z);
	}

	/**
	 * Block directive factory method.
	 * 
	 * The directive is created with a new immutable {@linkplain BlockPos} to avoid
	 * position changes after creation of the directive.
	 * 
	 * @param blockPos   position
	 * @param block      block
	 * @param blockState block state
	 * @param harvest    true if block should be harvested.
	 * 
	 * @return block directive. The directive is created with a new immutable block
	 *         position.
	 */
	public static BlockDirective getInstance(BlockPos blockPos, Block block, BlockState blockState, boolean harvest) {
		BlockDirective directive = new BlockDirective(blockPos.toImmutable(), block, harvest);
		directive.setState(blockState);
		return directive;
	}

	/**
	 * Block directive factory method.
	 * 
	 * The directive is created with a new immutable {@linkplain BlockPos} to avoid
	 * position changes after creation of the directive.
	 * 
	 * @param blockPos   position
	 * @param block      block
	 * @param blockState block state
	 * @param harvest    true if block should be harvested.
	 * @param player     player which should do the harvesting.
	 * 
	 * @return block directive. The directive is created with a new immutable block
	 *         position.
	 */
	public static BlockDirective getInstance(BlockPos blockPos, Block block, BlockState blockState, boolean harvest,
			PlayerEntity player) {
		BlockDirective directive = new BlockDirective(blockPos.toImmutable(), block, harvest, player);
		directive.setState(blockState);
		return directive;
	}

}