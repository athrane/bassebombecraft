package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.NULL_I18N_ARGS;
import static bassebombecraft.item.ItemUtils.resolveCompositeItemTypeFromString;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static net.minecraft.util.ActionResultType.SUCCESS;
import static net.minecraft.util.text.TextFormatting.DARK_BLUE;
import staticnet.minecraft.ChatFormattingg.GREEN;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.operator.Operator2;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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
	 * Item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	protected String tooltip;

	/**
	 * Constructor.
	 * 
	 * @param config item configuration.
	 */
	public GenericCompositeNullItem(ItemConfig config) {
		super(new Item.Properties().tab(getItemGroup()));

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
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

		return new InteractionResultHolder<ItemStack>(SUCCESS, playerIn.getItemInHand(handIn));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		String typeName = resolveCompositeItemTypeFromString(this);
		tooltip.add(new TranslatableComponent(GREEN + this.tooltip));
		tooltip.add(new TranslatableComponent("genericcompositenullitem.type", typeName));
		tooltip.add(new TranslatableComponent("genericcompositenullitem.usage", NULL_I18N_ARGS).withStyle(DARK_BLUE));
	}

	/**
	 * Return {@linkplain Operator2} for composite item.
	 * 
	 * @return operator for composite item.
	 */
	abstract public Operator2 createOperator();

}
