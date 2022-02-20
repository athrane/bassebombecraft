package bassebombecraft.item.book;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.item.composite.DefaultCompositeItemResolver.getInstance;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import bassebombecraft.client.event.rendering.GenericCompositeItemsBookRenderer;
import bassebombecraft.config.ItemConfig;
import bassebombecraft.item.composite.CompositeItemsResolver;
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
import net.minecraftforge.fml.RegistryObject;

/**
 * Generic book implementation for execution of operators derived from composite
 * items.
 * 
 * The operator is applied when the item is right clicked.
 * 
 * The used ports is reused for the duration of the item and updated for all
 * invocations of the operator. The ports is updated with the world and the
 * invoker entity when the item is right clicked.
 **/
public class GenericCompositeItemsBook extends Item {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Book item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * Composite item resolver.
	 */
	CompositeItemsResolver resolver;

	/**
	 * Constructor.
	 * 
	 * @param config   item configuration.
	 * @param ports    ports used by operators.
	 * @param operator operator executed when item is right clicked.
	 */
	@Deprecated
	public GenericCompositeItemsBook(ItemConfig config, Ports ports, Operator2 operator) {
		super(new Item.Properties().group(getItemGroup()));
		this.ports = ports;

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	/**
	 * Constructor.
	 * 
	 * @param config        item configuration.
	 * @param splComposites stream of composite items containing operators which are
	 *                      executed when item is right clicked.
	 */
	public GenericCompositeItemsBook(ItemConfig config, Supplier<Stream<RegistryObject<Item>>> splComposites) {
		super(new Item.Properties().group(getItemGroup()));
		this.ports = getInstance();
		this.resolver = getInstance(splComposites);

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		// exit if invoked at client side
		if (isLogicalClient(world)) {
			return super.onItemRightClick(world, player, hand);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// execute operator
		ports.setLivingEntity1(player);
		ports.setWorld(world);
		run(ports, resolver.getOperator());

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

		// only update the action at server side since we updates the world
		if (isLogicalClient(world))
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

	/**
	 * Get configured composite items as item stacks.
	 * 
	 * This method is used to support rendering in
	 * {@linkplain GenericCompositeItemsBookRenderer}.
	 * 
	 * @return configured composite items as item stacks.
	 */
	public ItemStack[] getCompositeItems() {
		return resolver.getItemStacks();
	}

}
