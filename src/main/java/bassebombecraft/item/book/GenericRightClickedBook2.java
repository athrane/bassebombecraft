package bassebombecraft.item.book;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
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
 * Generic Book implementation for execution of operators using
 * {@linkplain Operator2}.
 * 
 * The operators are applied when the item is right clicked.
 * 
 * The ports is updated with the world and the invoker entity when item is right
 * clicked.
 **/
public class GenericRightClickedBook2 extends Item {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators.
	 */
	Operator2[] ops;

	/**
	 * Book item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * constructor.
	 * 
	 * @param name   item name.
	 * @param config item configuration.
	 * @param ports  ports used by operators.
	 * @param ops    operators executed when item is right clicked.
	 */
	public GenericRightClickedBook2(String name, ItemConfig config, Ports ports, Operator2[] ops) {
		super(new Item.Properties().group(getItemGroup()));
		doCommonItemInitialization(this, name);

		this.ports = ports;
		this.ops = ops;

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
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

		// execute operators
		ports.setLivingEntity1(playerIn);
		ports.setWorld(worldIn);
		run(ports, ops);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// only update the action at server side since we updates the world
		if (isLogicalClient(worldIn))
			return;

		// NO-OP
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

}
