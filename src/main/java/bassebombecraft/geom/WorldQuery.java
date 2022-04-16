package bassebombecraft.geom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface for querying about the world.
 */
@Deprecated
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
	Player getPlayer();

	/**
	 * Return world.
	 * 
	 * @return game world.
	 */
	Level getWorld();
}
