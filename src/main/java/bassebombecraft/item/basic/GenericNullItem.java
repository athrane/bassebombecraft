package bassebombecraft.item.basic;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Generic NOP item implementation.
 * 
 * The object implement a NOP when the item is right clicked.
 */
public class GenericNullItem extends Item {

	/**
	 * Item action.
	 */
	RightClickedItemAction action;

	/**
	 * Item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * Constructor.
	 * 
	 * @param config item configuration.
	 * @param action item action object which is invoked when item is right clicked.
	 */
	public GenericNullItem(ItemConfig config, RightClickedItemAction action) {
		super(new Item.Properties().tab(getItemGroup()));
		this.action = action;

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		// exit if invoked at client side
		if (isLogicalClient(worldIn)) {
			return super.use(worldIn, playerIn, handIn);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), playerIn.getGameProfile().getName());

		// add cooldown
		ItemCooldowns tracker = playerIn.getCooldowns();
		tracker.addCooldown(this, coolDown);

		// apply action
		action.onRightClick(worldIn, playerIn);

		return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// only update the action at server side since we updates the world
		if (isLogicalClient(worldIn))
			return;

		action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		Component text = new TranslatableComponent(ChatFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

}
