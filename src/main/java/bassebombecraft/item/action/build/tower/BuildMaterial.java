package bassebombecraft.item.action.build.tower;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
	 * @param state         material state.
	 */
	public BuildMaterial(Block buildMaterial, BlockState state) {
		super();
		this.state = state;
		this.buildMaterial = buildMaterial;
	}

	public BlockState getState() {
		return state;
	}

	public Block getBlock() {
		return buildMaterial;
	}
}