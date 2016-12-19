package bassebombecraft.geom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldQueryImpl implements WorldQuery {

	World world;
	EntityPlayer player;
	BlockPos targetBlockPosition;
	boolean harvest;

	/**
	 * World query object.
	 * 
	 * Blocks processed by item will be harvested.
	 * 
	 * @param player
	 *            player object.
	 * @param targetBlockPosition
	 *            target block position that player interacted with.
	 */
	public WorldQueryImpl(EntityPlayer player, BlockPos targetBlockPosition) {
		super();
		this.player = player;
		this.world = player.getEntityWorld();
		this.targetBlockPosition = targetBlockPosition;
	}

	/**
	 * World query object.
	 * 
	 * @param player
	 *            player object.
	 * @param targetBlockPosition
	 *            target block position that player interacted with.
	 * @param harvest
	 *            define whether blocks processed by item should be harvested.
	 */
	public WorldQueryImpl(EntityPlayer player, BlockPos targetBlockPosition, boolean harvest) {
		super();
		this.player = player;
		this.world = player.getEntityWorld();
		this.targetBlockPosition = targetBlockPosition;
		this.harvest = harvest;
	}

	@Override
	public BlockPos getTargetBlockPosition() {
		return targetBlockPosition;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean isWorldAtClientSide() {
		return world.isRemote;
	}

	@Override
	public boolean harvestBlocks() {
		return harvest;
	}

}
