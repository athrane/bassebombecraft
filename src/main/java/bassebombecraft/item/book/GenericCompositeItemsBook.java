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
import net.minecraftforge.registries.RegistryObject;

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
	 * @param config        item configuration.
	 * @param splComposites stream of composite items containing operators which are
	 *                      executed when item is right clicked.
	 */
	public GenericCompositeItemsBook(ItemConfig config, Supplier<Stream<RegistryObject<Item>>> splComposites) {
		super(new Item.Properties().tab(getItemGroup()));
		this.ports = getInstance();
		this.resolver = getInstance(splComposites);

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity entityTarget, LivingEntity entityUser) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

		// exit if invoked at client side
		if (isLogicalClient(world)) {
			return super.use(world, player, hand);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// add cooldown
		ItemCooldowns tracker = player.getCooldowns();
		tracker.addCooldown(this, coolDown);

		// execute operator
		ports.setLivingEntity1(player);
		ports.setWorld(world);
		run(ports, resolver.getOperator());

		return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {

		// only update the action at server side since we updates the world
		if (isLogicalClient(world))
			return;

		// NO-OP
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		Component text = new TranslatableComponent(ChatFormatting.GREEN + this.tooltip);
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
