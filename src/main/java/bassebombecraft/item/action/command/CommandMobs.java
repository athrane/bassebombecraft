package bassebombecraft.item.action.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which issues mob
 * commands.
 */
public class CommandMobs implements RightClickedItemAction {

	@Override
	public void onRightClick(Level world, LivingEntity entity) {
		try {
			// get repository
			MobCommanderRepository repository = getProxy().getServerMobCommanderRepository();

			// exit if not a player
			if (!PlayerUtils.isTypePlayerEntity(entity))
				return;

			// typecast
			Player player = (Player) entity;

			// register player
			repository.register(player);

			// cycle command
			repository.cycle(player);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}
}
