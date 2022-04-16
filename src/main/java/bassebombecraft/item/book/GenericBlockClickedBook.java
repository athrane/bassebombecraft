package bassebombecraft.item.book;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BOOKS_CONFIGPATH;
import static bassebombecraft.ModConstants.ITEM_BOOK_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.config.ConfigUtils.resolveTooltip;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.item.action.BlockClickedItemAction;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when a block is right clicked with this item.
 */
public class GenericBlockClickedBook extends Item {

	/**
	 * Item action.
	 */
	BlockClickedItemAction action;

	/**
	 * Book item cooldown value.
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
	public GenericBlockClickedBook(ItemConfig config, BlockClickedItemAction action) {
		super(new Item.Properties().tab(getItemGroup()));
		this.action = action;

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	/**
	 * Constructor.
	 * 
	 * @param name   item name.
	 * @param action item action object which is invoked when item is right clicked.
	 */
	@Deprecated
	public GenericBlockClickedBook(String name, BlockClickedItemAction action) {
		super(new Item.Properties().tab(getItemGroup()));
		this.action = action;

		// get cooldown or default value
		String configPath = BOOKS_CONFIGPATH + name;
		coolDown = resolveCoolDown(configPath, ITEM_BOOK_DEFAULT_COOLDOWN);
		tooltip = resolveTooltip(configPath, ITEM_DEFAULT_TOOLTIP);
	}

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		// exit if invoked at client side
		if (isLogicalClient(context.getLevel())) {
			return super.useOn(context);
		}

		// post analytics
		Player player = context.getPlayer();
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// add cooldown
		ItemCooldowns tracker = player.getCooldowns();
		tracker.addCooldown(this, coolDown);

		return action.onItemUse(context);
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
