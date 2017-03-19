package bassebombecraft.item.action.build.tower;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * Data object for specification of stairs materials.
 */
public class StairsMaterial {

	IBlockState state;
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
	public StairsMaterial(IBlockState state, Block stairMaterial, Block solidMaterial) {
		super();
		this.state = state;
		this.stairMaterial = stairMaterial;
		this.solidMaterial = solidMaterial;
	}

	public IBlockState getState() {
		return state;
	}

	public Block getStairMaterial() {
		return stairMaterial;
	}

	public Block getSolidMaterial() {
		return solidMaterial;
	}
}