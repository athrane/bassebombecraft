package bassebombecraft.item.action.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which issues mob
 * commands.
 */
public class CommandMobs implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// get repository
		MobCommanderRepository repository = getBassebombeCraft().getMobCommanderRepository();

		// exit if not a player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// typecast
		PlayerEntity player = (PlayerEntity) entity;

		// register player
		repository.register(player);

		// cycle command
		repository.cycle(player);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}
}
