package bassebombecraft.item.basic;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.ITEM_BASICITEM_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.config.ConfigUtils.resolveTooltip;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import java.util.List;

import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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
	 * Generic null item constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param action
	 *            item action object which is invoked when item is right
	 *            clicked.
	 */
	public GenericNullItem(String name, RightClickedItemAction action) {
		doCommonItemInitialization(this, name);
		
		this.action = action;

		// get cooldown or default value
		coolDown = resolveCoolDown(name, ITEM_BASICITEM_DEFAULT_COOLDOWN);
		tooltip = resolveTooltip(name, ITEM_DEFAULT_TOOLTIP);
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityTarget, EntityLivingBase entityUser) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		// exit if invoked at client side
		if (isWorldAtClientSide(worldIn)) {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		// post analytics
		getProxy().postItemUsage(this.getUnlocalizedName(), playerIn.getName());

		// add cooldown
		CooldownTracker tracker = playerIn.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// apply action
		action.onRightClick(worldIn, playerIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GREEN + this.tooltip);
	}
	
}
