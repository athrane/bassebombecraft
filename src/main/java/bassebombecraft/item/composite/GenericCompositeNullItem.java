package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import net.minecraft.client.util.ITooltipFlag;
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
 * Generic item implementation for null composite items.
 * 
 * The object implements no logic when the item is right clicked.
 * 
 * Abstract class intended to extended by sub classes.
 */
public abstract class GenericCompositeNullItem extends Item {

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
	 * @param name   item name.
	 * @param config item configuration.
	 */
	public GenericCompositeNullItem(String name, ItemConfig config) {
		super(new Item.Properties().group(getItemGroup()));
		doCommonItemInitialization(this, name);

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		// exit if invoked at client side
		if (isLogicalClient(worldIn)) {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), playerIn.getGameProfile().getName());

		// add cooldown
		CooldownTracker tracker = playerIn.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

	/**
	 * Return {@linkplain Operator2} for composite item.
	 * 
	 * @return operator for composite item.
	 */
	abstract public Operator2 createOperator();
		
}