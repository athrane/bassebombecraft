package bassebombecraft.inventory.container;

import static bassebombecraft.ModConstants.COMPOSITE_MAX_SIZE;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.item.ItemUtils.isTypeCompositeItem;

import java.util.ArrayList;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Subclass of {@linkplain ItemStackHandler} which implements the item
 * inventory.
 * 
 * The class enforces addition of composite items only to the composite item
 * inventory.
 */
public class CompositeMagicItemItemStackHandler extends ItemStackHandler {

	/**
	 * Name of item prefixes.
	 */
	static final String ITEM_NAME_PREFIX = MODID + ":";

	/**
	 * Tracks if content has changed.
	 */
	boolean isChanged = true;

	/**
	 * Item signature.
	 */
	Item[] signatures = new Item[COMPOSITE_MAX_SIZE];

	/**
	 * Operator.
	 */
	Operator2 operator;

	/**
	 * Composite name.
	 */
	String compositeName;

	/**
	 * Length of current composite sequence.
	 */
	int compositeLength;

	/**
	 * Inventory index for current composite sequence.
	 */
	int inventoryIndex;

	/**
	 * Constructor.
	 * 
	 * @param size item stack size.
	 */
	public CompositeMagicItemItemStackHandler(int size) {
		super(size);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {

		// exit if empty item stack
		if (stack.isEmpty())
			return false;

		// validate item is an composite item
		Item item = stack.getItem();
		return isTypeCompositeItem(item);
	}

	@Override
	protected void onContentsChanged(int slot) {
		isChanged = true;

		inventoryIndex = findInventoryComposites();
		if (isInventoryContainingComposites(inventoryIndex)) {

			compositeLength = calculateCompositeLength(inventoryIndex);
			compositeLength = Math.min(compositeLength, COMPOSITE_MAX_SIZE);

			// configure operators
			if (hasSignatureChanged(inventoryIndex, compositeLength)) {
				resolveOperators(inventoryIndex, compositeLength);
				createCompositeName(inventoryIndex, compositeLength);
			}
		} else {
			compositeLength = 0;
		}
	}

	/**
	 * return true if content has changed.
	 * 
	 * Reset state after each invocation.
	 * 
	 * @return true if content has changed.
	 */
	public boolean isChanged() {
		boolean retval = isChanged;
		isChanged = false;
		return retval;
	}

	/**
	 * Scan the inventory for composite items.
	 * 
	 * Returns index of the first occurrence of a composite item.
	 * 
	 * Return -1 if no composite items is found.
	 * 
	 * @return Returns index of the first occurrence of a composite item. Return -1
	 *         if no composite items is found.
	 */
	int findInventoryComposites() {
		int size = stacks.size();
		for (int index = 0; index < size; index++) {
			ItemStack inventoryStack = getStackInSlot(index);

			// skip item stack if empty
			if (inventoryStack.isEmpty())
				continue;

			// exit loop if composite item
			if (isCompositeItem(index))
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
	 * @param index inventory index to test for composite item.
	 * 
	 * @return true if item in inventory slot is a composite item.
	 */
	boolean isCompositeItem(int index) {
		ItemStack stack = getStackInSlot(index);
		Item inventoryItem = stack.getItem();
		return isTypeCompositeItem(inventoryItem);
	}

	/**
	 * Calculate the length of the composite.
	 * 
	 * The length is computed from the sequence of connected composite items.
	 * 
	 * @param inventoryIndex inventory index for first composite item.
	 * 
	 * @return length of the composite.
	 */
	int calculateCompositeLength(int inventoryIndex) {
		int length = 0;
		int size = stacks.size();
		for (int index = inventoryIndex; index < size; index++) {

			// exit loop if not a composite item
			if (!isCompositeItem(index))
				return length;

			// update length
			length++;
		}

		return length;
	}

	/**
	 * Calculate if signature of composite has changed.
	 * 
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 * 
	 * @return true if signature of composites has changed.
	 */
	boolean hasSignatureChanged(int inventoryIndex, int length) {
		boolean retval = false;

		for (int index = 0; index < length; index++) {

			// determine if signature is changed
			boolean hasChanged = hasSignatureChangedAtSlot(inventoryIndex + index, index);

			// if changed then update state and signature
			if (hasChanged) {

				// update signature if changed
				updateSignatureAtSlot(inventoryIndex, index);

				// update state
				retval = true;
			}
		}

		return retval;
	}

	/**
	 * Calculate if signature of composite has changed.
	 * 
	 * @param inventoryIndex inventory index for composite item.
	 * @param signatureIndex signature for composite item.
	 * 
	 * @return true if signature of composites has changed.
	 */
	boolean hasSignatureChangedAtSlot(int inventoryIndex, int signatureIndex) {

		// get inventory item
		ItemStack inventoryStack = getStackInSlot(inventoryIndex);
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
	 * @param inventoryIndex inventory index for composite item.
	 * @param signatureIndex signature for composite item.
	 */
	void updateSignatureAtSlot(int inventoryIndex, int signatureIndex) {
		ItemStack inventoryStack = getStackInSlot(inventoryIndex);
		Item inventoryItem = inventoryStack.getItem();
		signatures[signatureIndex] = inventoryItem;
	}

	/**
	 * Resolve operators from composite items.
	 * 
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 */
	void resolveOperators(int inventoryIndex, int length) {
		ArrayList<Operator2> opList = new ArrayList<Operator2>();

		for (int index = 0; index < length; index++) {
			resolveOperator(opList, inventoryIndex + index);
		}

		// create array
		Operator2[] opArray = opList.toArray(new Operator2[length]);

		// create sequence operator
		this.operator = new Sequence2(opArray);
	}

	/**
	 * Resolve operator from item stack containing composite item.
	 * 
	 * @param opList list of operators where operator is added to.
	 * @param index  inventory index for composite item.
	 */
	void resolveOperator(ArrayList<Operator2> opList, int index) {

		// get inventory item
		ItemStack inventoryStack = getStackInSlot(index);
		Item inventoryItem = inventoryStack.getItem();

		// type cast
		GenericCompositeNullItem compositeNullItem = (GenericCompositeNullItem) inventoryItem;

		// add operator
		Operator2 operator = compositeNullItem.createOperator();
		opList.add(operator);
	}

	/**
	 * Get configured operator.
	 * 
	 * @return configured operator.
	 */
	public Operator2 getOperator() {
		return operator;
	}

	/**
	 * Create name for current composite.
	 * 
	 * @param inventoryIndex inventory index for first composite item.
	 * @param length         length of the composite in the inventory.
	 * 
	 * @return array containing the operators in the composite.
	 */
	void createCompositeName(int inventoryIndex, int length) {
		StringBuilder name = new StringBuilder();

		for (int index = 0; index < length; index++) {

			// get inventory item
			ItemStack inventoryStack = getStackInSlot(inventoryIndex + index);
			Item inventoryItem = inventoryStack.getItem();
			name.append("-");

			// get name without mod prefix
			String itemName = inventoryItem.getRegistryName().toString();

			String removedPrefixName = itemName.substring(ITEM_NAME_PREFIX.length());
			name.append(removedPrefixName);
		}

		this.compositeName = name.toString();
	}

	/**
	 * Get composite name.
	 * 
	 * The name is concatenated from then names of the configured composite items.
	 * 
	 * @return composite name.
	 */
	public String getCompositeName() {
		return compositeName;
	}

	/**
	 * Get length of current composite sequence.
	 * 
	 * @return length of current composite sequence.
	 */
	public int getCompositeLength() {
		return compositeLength;
	}

	/**
	 * Get inventory index of current composite sequence.
	 * 
	 * @return index of current composite sequence.
	 */
	public int getCompositeInventoryIndex() {
		return inventoryIndex;
	}

}
