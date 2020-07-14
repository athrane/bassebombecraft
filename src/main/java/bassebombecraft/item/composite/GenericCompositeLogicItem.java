package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static bassebombecraft.ModConstants.*;
import bassebombecraft.config.ItemConfig;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
 * Generic item implementation for execution of composite logic.
 * 
 * The logic is applied when the item is right clicked.
 **/
public class GenericCompositeLogicItem extends Item {

	/**
	 * Item signature.
	 */
	Item[] signatures = new Item[SIGNATURE_SIZE];

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators.
	 */
	Operator2 ops;

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
	 */
	public GenericCompositeLogicItem(String name, ItemConfig config) {
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

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// skip configuration if inventory dons't contain composites
		int inventoryIndex = findInventoryComposites(player);
		if (isInventoryContainingComposites(inventoryIndex)) {

			// configure
			boolean hasSignatureChanged = hasSignatureChanged(player, inventoryIndex);
			if (hasSignatureChanged)
				configureState(player, inventoryIndex);
		}

		// execute operators
		ports.setLivingEntity1(player);
		ports.setWorld(world);
		run(ports, ops);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Scan the inventory for composite items. Returns index of the first occurrence
	 * of a composite item. Return -1 if no composite items is found.
	 * 
	 * @param inventory player inventor
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

			// get item
			Item inventoryItem = inventoryStack.getItem();

			// exit loop if composite item
			if (inventoryItem instanceof GenericCompositeNullItem)
				return index;

		}

		// no composite values where found
		return -1;
	}

	/**
	 * Returns true if inventory contain a composite item, i.e. index isn't -1.
	 * 
	 * @param inventoryIndex
	 * 
	 * @return true if inventory contain a composite item, i.e. index isn't -1.
	 */
	boolean isInventoryContainingComposites(int inventoryIndex) {
		return (inventoryIndex != -1);
	}

	/**
	 * Calculate if signature of composite has changed.
	 * 
	 * @param player player whose inventory is processed.
	 * @param index  inventory index for first composite item.
	 * 
	 * @return true if signature of composites has changed.
	 */
	boolean hasSignatureChanged(PlayerEntity player, int index) {
		boolean retval = false;

		for (int signatureIndex = 0; signatureIndex < SIGNATURE_SIZE; signatureIndex++) {

			// determine if signature is changed
			boolean hasChanged = hasSignatureChangedAtSlot(player, index, signatureIndex);

			if (hasChanged) {
				
				// update signature if changed				
				updateSignatureAtSlot(player, index, signatureIndex);

				// update state				
				retval = true;				
			}

			// update inventory index
			index++;
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

		// get hashes
		int signatureHash = signatureItem.hashCode();
		int inventoryItemHash = inventoryItem.hashCode();

		if (signatureHash == inventoryItemHash)
			return false;

		// set signature
		signatures[signatureIndex] = inventoryItem;
		return true;
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
	 * @param player player whose inventory is processed.
	 * @param index  inventory index for first composite item.
	 */
	void configureState(PlayerEntity player, int index) {
		PlayerInventory inventory = player.inventory;

		ArrayList<Operator2> opList = new ArrayList<Operator2>();

		// configure operators
		configureOperator(inventory, opList, index);
		configureOperator(inventory, opList, index + 1);
		configureOperator(inventory, opList, index + 2);

		// create array
		Operator2[] opArray = opList.toArray(new Operator2[2]);

		// create sequence operator
		this.ops = new Sequence2(opArray);

	}

	/**
	 * 
	 * @param inventory
	 * @param opList
	 * @param index
	 */
	void configureOperator(PlayerInventory inventory, ArrayList<Operator2> opList, int index) {

		// get hotbar item
		ItemStack hotbarSlot = inventory.getStackInSlot(index);
		Item hotbarItem = hotbarSlot.getItem();

		// add operator for current slot
		if (hotbarItem instanceof GenericCompositeNullItem) {

			// type cast
			GenericCompositeNullItem compositeNullItem = (GenericCompositeNullItem) hotbarItem;

			// add operator
			Operator2 operator = compositeNullItem.createOperator();
			opList.add(operator);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		String message = new StringBuilder().append(this.tooltip).append(" (hc=").append(this.hashCode())
				.append(" ,op=").append(this.ops).append(")").toString();
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + message);
		tooltip.add(text);
	}

}
