package bassebombecraft.item.book;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.ITEM_BOOK_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.config.ConfigUtils.resolveTooltip;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when the item is right clicked.
 */
public class GenericRightClickedBook extends Item {

	/**
	 * Item action.
	 */
	RightClickedItemAction action;

	/**
	 * Book item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * Generic book constructor.
	 * 
	 * @param name   item name.
	 * @param action item action object which is invoked when item is right clicked.
	 */
	public GenericRightClickedBook(String name, RightClickedItemAction action) {
		super(new Item.Properties().group(getItemGroup()));
		doCommonItemInitialization(this, name);

		this.action = action;

		// get cooldown or default value
		coolDown = resolveCoolDown(name, ITEM_BOOK_DEFAULT_COOLDOWN);
		tooltip = resolveTooltip(name, ITEM_DEFAULT_TOOLTIP);
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		// exit if invoked at client side
		if (isWorldAtClientSide(worldIn)) {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), playerIn.getGameProfile().getName());

		// add cooldown
		CooldownTracker tracker = playerIn.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// apply action
		action.onRightClick(worldIn, playerIn);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

}
