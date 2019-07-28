package bassebombecraft.item.action.build.tower;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * Data object for specification of build materials with meta data.
 */
public class BuildMaterial {

	BlockState state;
	Block buildMaterial;

	/**
	 * BuildMaterial constructor.
	 * 
	 * @param buildMaterial build material.
	 * @param state         stair material state.
	 */
	public BuildMaterial(Block buildMaterial, BlockState state) {
		super();
		this.state = state;
		this.buildMaterial = buildMaterial;
	}

	/**
	 * FloorMaterial constructor. Material is created with default state.
	 * 
	 * @param floorMaterial solid material.
	 */
	public BuildMaterial(Block floorMaterial) {
		super();
		this.state = floorMaterial.getDefaultState();
		this.buildMaterial = floorMaterial;
		;
	}

	public BlockState getState() {
		return state;
	}

	public Block getBlock() {
		return buildMaterial;
	}
}