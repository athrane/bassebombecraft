package bassebombecraft.geom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Deprecated
public class WorldQueryImpl implements WorldQuery {

	Level world;
	Player player;
	BlockPos targetBlockPosition;
	boolean harvest;

	/**
	 * World query object.
	 * 
	 * Blocks processed by item will be harvested.
	 * 
	 * @param player              player object.
	 * @param targetBlockPosition target block position that player interacted with.
	 */
	public WorldQueryImpl(Player player, BlockPos targetBlockPosition) {
		super();
		this.player = player;
		this.world = player.getCommandSenderWorld();
		this.targetBlockPosition = targetBlockPosition;
	}

	@Override
	public BlockPos getTargetBlockPosition() {
		return targetBlockPosition;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public Level getWorld() {
		return world;
	}

}
