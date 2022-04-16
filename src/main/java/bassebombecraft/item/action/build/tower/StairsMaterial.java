package bassebombecraft.item.action.build.tower;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Data object for specification of stairs materials.
 */
public class StairsMaterial {

	BlockState state;
	Block stairMaterial;
	Block solidMaterial;

	/**
	 * StairsMaterial constructor.
	 * 
	 * @param state
	 *            stair material state.
	 * @param stairMaterial
	 *            stair material.
	 * @param solidMaterial
	 *            solid material.
	 */
	public StairsMaterial(BlockState state, Block stairMaterial, Block solidMaterial) {
		super();
		this.state = state;
		this.stairMaterial = stairMaterial;
		this.solidMaterial = solidMaterial;
	}

	public BlockState getState() {
		return state;
	}

	public Block getStairMaterial() {
		return stairMaterial;
	}

	public Block getSolidMaterial() {
		return solidMaterial;
	}
}