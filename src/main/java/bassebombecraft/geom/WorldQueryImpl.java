package bassebombecraft.geom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class WorldQueryImpl implements WorldQuery {

	World world;
	PlayerEntity player;
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
	public WorldQueryImpl(PlayerEntity player, BlockPos targetBlockPosition) {
		super();
		this.player = player;
		this.world = player.getEntityWorld();
		this.targetBlockPosition = targetBlockPosition;
	}

	@Override
	public BlockPos getTargetBlockPosition() {
		return targetBlockPosition;
	}

	@Override
	public PlayerEntity getPlayer() {
		return player;
	}

	@Override
	public World getWorld() {
		return world;
	}

}
