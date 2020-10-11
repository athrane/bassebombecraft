package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.COMPOSITE_MAX_SIZE;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.ModConfiguration.compositeMagicItem;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraftforge.fml.network.NetworkHooks.openGui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.inventory.container.CompositeMagicItemCapabilityProvider;
import bassebombecraft.inventory.container.CompositeMagicItemContainerProvider;
import bassebombecraft.inventory.container.CompositeMagicItemItemStackHandler;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
	 * Name of item prefixes.
	 */
	static final String ITEM_NAME_PREFIX = MODID + ":";

	/**
	 * Item signature.
	 */
	Item[] signatures = new Item[COMPOSITE_MAX_SIZE];

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operator.
	 */
	Operator2 operator;

	/**
	 * Composite name.
	 */
	String compositeName;

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

		/**
		// skip configuration if inventory dons't contain composites
		int inventoryIndex = findInventoryComposites(player);
		if (isInventoryContainingComposites(inventoryIndex)) {

			// calculate composite length
			int compositeLength = calculateCompositeLength(player, inventoryIndex);
			compositeLength = Math.min(compositeLength, COMPOSITE_MAX_SIZE);

			// configure operators
			if (hasSignatureChanged(player, inventoryIndex, compositeLength)) {
				configureOperators(player, inventoryIndex, compositeLength);
				createCompositeName(player, inventoryIndex, compositeLength);
			}
		}
		**/
		
		// get operators
		operator = inventory.getOperator();

		// post analytics
		//getProxy().postItemUsage(compositeName, player.getGameProfile().getName());

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
	 * Scan the inventory for composite items.
	 * 
	 * Returns index of the first occurrence of a composite item.
	 * 
	 * Return -1 if no composite items is found.
	 * 
	 * @param inventory player inventory.
	 * 
	 * @return Returns index of the first occurrence of a composite item. Return -1
	 *         if no composite items is found.
	 */
	int findInventoryComposites(PlayerEntity player) {
		PlayerInventory inventory = player.inventory;

		for (int index = 0; index < inventory.getSizeInventory(); index++) {
			ItemStack inventoryStack = inventory.getStackInSlot(index);

			// skip item stack if empty
			if (inventoryStack.isEmpty())
				continue;

			// exit loop if composite item
			if (isCompositeItem(player, index))
				return index;
		}

		// no composite values where found
		return -1;
	}

	/**
	 * Returns true if inventory contain a composite item, i.e. index isn't -1.
	 * 
	 * @param index inventory index.
	 * 
	 * @return true if inventory contain a composite item, i.e. index isn't -1.
	 */
	boolean isInventoryContainingComposites(int inventoryIndex) {
		return (inventoryIndex != -1);
	}

	/**
	 * Returns true if item in inventory slot is a composite item.
	 * 
	 * @param player player whose inventory is processed.
	 * @param index  inventory index to test for composite item.
	 * 
	 * @return true if item in inventory slot is a composite item.
	 */
	boolean isCompositeItem(PlayerEntity player, int index) {
		PlayerInventory inventory = player.inventory;

		// get inventory item
		ItemStack inventoryStack = inventory.getStackInSlot(index);
		Item inventoryItem = inventoryStack.getItem();

		// add operator for current slot
		return (inventoryItem instanceof GenericCompositeNullItem);
	}

	/**
	 * Calculate the length of the composite.
	 * 
	 * The length is computed from the sequence of connected
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for first composite item.
	 * 
	 * @return length of the composite.
	 */
	int calculateCompositeLength(PlayerEntity player, int inventoryIndex) {
		PlayerInventory inventory = player.inventory;
		int length = 0;

		for (int index = inventoryIndex; index < inventory.getSizeInventory(); index++) {

			// exit loop if not a composite item
			if (!isCompositeItem(player, index))
				return length;

			// update length
			length++;
		}

		return length;
	}

	/**
	 * Calculate if signature of composite has changed.
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 * 
	 * @return true if signature of composites has changed.
	 */
	boolean hasSignatureChanged(PlayerEntity player, int inventoryIndex, int length) {
		boolean retval = false;

		for (int index = 0; index < length; index++) {

			// determine if signature is changed
			boolean hasChanged = hasSignatureChangedAtSlot(player, inventoryIndex + index, index);

			// if changed then update state and signature
			if (hasChanged) {

				// update signature if changed
				updateSignatureAtSlot(player, inventoryIndex, index);

				// update state
				retval = true;
			}
		}

		return retval;
	}

	/**
	 * Calculate if signature of composite has changed.
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for composite item.
	 * @param signatureIndex signature for composite item.
	 * 
	 * @return true if signature of composites has changed.
	 */
	boolean hasSignatureChangedAtSlot(PlayerEntity player, int inventoryIndex, int signatureIndex) {
		PlayerInventory inventory = player.inventory;

		// get inventory item
		ItemStack inventoryStack = inventory.getStackInSlot(inventoryIndex);
		Item inventoryItem = inventoryStack.getItem();

		// get signature item
		Item signatureItem = signatures[signatureIndex];

		// if no signature item is define capture it
		if (signatureItem == null) {
			signatures[signatureIndex] = inventoryItem;
			return true;
		}

		// get and compare hashes
		int signatureHash = signatureItem.hashCode();
		int inventoryItemHash = inventoryItem.hashCode();
		return (!(signatureHash == inventoryItemHash));
	}

	/**
	 * Update signature.
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for composite item.
	 * @param signatureIndex signature for composite item.
	 */
	void updateSignatureAtSlot(PlayerEntity player, int inventoryIndex, int signatureIndex) {
		PlayerInventory inventory = player.inventory;
		ItemStack inventoryStack = inventory.getStackInSlot(inventoryIndex);
		Item inventoryItem = inventoryStack.getItem();
		signatures[signatureIndex] = inventoryItem;
	}

	/**
	 * Configure state of composites.
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 * 
	 * @return array containing the operators in the composite.
	 */
	void configureOperators(PlayerEntity player, int inventoryIndex, int length) {
		ArrayList<Operator2> opList = new ArrayList<Operator2>();

		for (int index = 0; index < length; index++) {
			configureOperator(player, opList, inventoryIndex + index);
		}

		// create array
		Operator2[] opArray = opList.toArray(new Operator2[length]);

		// create sequence operator
		this.operator = new Sequence2(opArray);
	}

	/**
	 * Configure operator.
	 * 
	 * @param player player whose inventory is processed.
	 * @param opList list of operators where operator is added to.
	 * @param index  inventory index for composite item.
	 */
	void configureOperator(PlayerEntity player, ArrayList<Operator2> opList, int index) {
		PlayerInventory inventory = player.inventory;

		// get inventory item
		ItemStack inventoryStack = inventory.getStackInSlot(index);
		Item inventoryItem = inventoryStack.getItem();

		// type cast
		GenericCompositeNullItem compositeNullItem = (GenericCompositeNullItem) inventoryItem;

		// add operator
		Operator2 operator = compositeNullItem.createOperator();
		opList.add(operator);
	}

	/**
	 * Create name for current composite.
	 * 
	 * @param player         player whose inventory is processed.
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 * 
	 * @return array containing the operators in the composite.
	 */
	void createCompositeName(PlayerEntity player, int inventoryIndex, int length) {
		PlayerInventory inventory = player.inventory;
		StringBuilder name = new StringBuilder();
		name.append(this.getRegistryName().toString());

		for (int index = 0; index < length; index++) {

			// get inventory item
			ItemStack inventoryStack = inventory.getStackInSlot(inventoryIndex + index);
			Item inventoryItem = inventoryStack.getItem();
			name.append("-");

			// get name without mod prefix
			String itemName = inventoryItem.getRegistryName().toString();

			String removedPrefixName = itemName.substring(ITEM_NAME_PREFIX.length());
			name.append(removedPrefixName);
		}

		this.compositeName = name.toString();
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
