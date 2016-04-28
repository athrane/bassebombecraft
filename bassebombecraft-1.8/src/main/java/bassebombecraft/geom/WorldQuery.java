package bassebombecraft.geom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Interface for querying about the world.
 */
public interface WorldQuery {

	/**
	 * Get block position that player interacted with.
	 * 
	 * @return block position that player interacted with;
	 */
	BlockPos getTargetBlockPosition();

	/**
	 * Return player.
	 * 
	 * @return player object.
	 */
	EntityPlayer getPlayer();

	/**
	 * Return world.
	 * 
	 * @return game world.
	 */
	World getWorld();

	/**
	 * return true if world is located at client side.
	 * 
	 * @return true if world is located at client side.
	 */
	boolean isWorldAtClientSide();

	/**
	 * Returns true if blocks processed by item should be harvested.
	 *  
	 * @return true if blocks processed by item should be harvested.
	 */
	boolean harvestBlocks();
}
