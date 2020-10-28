package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.compositeMagicItem;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraftforge.fml.network.NetworkHooks.openGui;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.config.ItemConfig;
import bassebombecraft.inventory.container.CompositeMagicItemCapabilityProvider;
import bassebombecraft.inventory.container.CompositeMagicItemContainerProvider;
import bassebombecraft.inventory.container.CompositeMagicItemItemStackHandler;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Composite magic item.
 * 
 * The composite logic is applied when the item is right clicked.
 * 
 * The used ports is reused for the duration of the item and updated for all
 * invocations of the operator. The ports is updated with the world and the
 * player entity when the item is right clicked.
 **/
public class CompositeMagicItem extends Item {

	/**
	 * Item identifier.
	 */
	public static final String NAME = CompositeMagicItem.class.getSimpleName();

	/**
	 * Base NBT tag.
	 */
	static final String BASE_NBT_TAG = "base";

	/**
	 * Capability NBT tag.
	 */
	static final String CAPABILITY_NBT_TAG = "cap";

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
	 * Constructor.
	 * 
	 * @param name   item name.
	 * @param config item configuration.
	 */
	public CompositeMagicItem() {
		this(NAME, compositeMagicItem);
	}

	/**
	 * Constructor.
	 * 
	 * @param name   item name.
	 * @param config item configuration.
	 */
	public CompositeMagicItem(String name, ItemConfig config) {
		super(new Item.Properties().group(getItemGroup()));
		doCommonItemInitialization(this, name);

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();

		// create ports
		this.ports = DefaultPorts.getInstance();

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		// exit if invoked at client side
		if (isLogicalClient(world)) {
			return super.onItemRightClick(world, player, hand);
		}

		// handle GUI, exit if GUI was opened
		if (openGUI(player, hand)) {
			return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
		}

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// get composite item inventory
		ItemStack itemStack = player.getHeldItem(hand);
		CompositeMagicItemItemStackHandler inventory = getItemStackHandler(itemStack);

		// post analytics
		String compositeName = createCompositeName(inventory);
		getProxy().postItemUsage(compositeName, player.getGameProfile().getName());

		// get operators
		Operator2 operator = inventory.getOperator();

		// execute operators
		ports.setLivingEntity1(player);
		ports.setWorld(world);
		run(ports, operator);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Open GUI.
	 * 
	 * @param player player
	 * @param hand   player hand
	 * 
	 * @return true if GUI was opened.
	 */
	boolean openGUI(PlayerEntity player, Hand hand) {

		// exit if item isn't held in main hand
		if (hand != MAIN_HAND)
			return false;

		// exit if SHIFT isn't pressed
		if (!player.isShiftKeyDown())
			return false;

		// get held item
		ItemStack itemStack = player.getHeldItem(hand);

		// open GUI
		CompositeMagicItemContainerProvider provider = new CompositeMagicItemContainerProvider(itemStack);
		openGui((ServerPlayerEntity) player, provider);

		return true;
	}

	/**
	 * Create name for current composite.
	 * 
	 * @param inventory composite item inventory.
	 * @return
	 * 
	 * @return name for current composite.
	 */
	String createCompositeName(CompositeMagicItemItemStackHandler inventory) {
		StringBuilder name = new StringBuilder();
		name.append(this.getRegistryName().toString());
		name.append(inventory.getCompositeName());
		return name.toString();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new CompositeMagicItemCapabilityProvider();
	}

	@Override
	public void readShareTag(ItemStack stack, CompoundNBT nbt) {

		// if nbt is null, set null tag en exit
		if (nbt == null) {
			stack.setTag(null);
			return;
		}

		// set base tag, is empty if not found
		CompoundNBT baseTag = nbt.getCompound(BASE_NBT_TAG);
		stack.setTag(baseTag);

		// set capability tag, is empty if not found
		CompoundNBT capabilityTag = nbt.getCompound(CAPABILITY_NBT_TAG);
		CompositeMagicItemItemStackHandler itemStackHandler = getItemStackHandler(stack);
		itemStackHandler.deserializeNBT(capabilityTag);
	}

	@Override
	public CompoundNBT getShareTag(ItemStack stack) {

		// get item handler
		CompositeMagicItemItemStackHandler itemStackHandler = getItemStackHandler(stack);

		// get tags
		CompoundNBT baseTag = stack.getTag();
		CompoundNBT capabilityTag = itemStackHandler.serializeNBT();

		// combine tags
		CompoundNBT combinedTag = new CompoundNBT();
		if (baseTag != null) {
			combinedTag.put(BASE_NBT_TAG, baseTag);
		}
		if (capabilityTag != null) {
			combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
		}
		return combinedTag;
	}

	/**
	 * Get inventory for item stack, handler is retrieved form capability.
	 * 
	 * @param itemStack item stack to get inventory for.
	 * 
	 * @return inventory for item stack.
	 */
	public static CompositeMagicItemItemStackHandler getItemStackHandler(ItemStack itemStack) {
		IItemHandler inventory = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

		// return dummy inventory if inventory can't be resolved
		if (inventory == null) {
			getBassebombeCraft().reportAndLogError("Composite item did not have the expected ITEM_HANDLER_CAPABILITY");
			return new CompositeMagicItemItemStackHandler(1);
		}

		// type cast and return inventory
		return (CompositeMagicItemItemStackHandler) inventory;
	}

}
