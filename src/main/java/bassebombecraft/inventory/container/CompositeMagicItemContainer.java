package bassebombecraft.inventory.container;

import static bassebombecraft.ModConstants.COMPOSITE_MAX_SIZE;
import static bassebombecraft.inventory.container.RegisteredContainers.COMPOSITE_ITEM_COMTAINER;

import bassebombecraft.item.composite.CompositeMagicItem;
import bassebombecraft.world.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Container implementation for {@linkplain CompositeMagicItem}
 */
public class CompositeMagicItemContainer extends Container {

	/**
	 * Container identifier.
	 */
	public static final String NAME = CompositeMagicItemContainer.class.getSimpleName();

	/**
	 * Container offset.
	 */
	static final int SLOT_X_SPACING = 18;

	/**
	 * Container offset.
	 */
	static final int SLOT_Y_SPACING = 18;

	/**
	 * Container offset.
	 */
	static final int HOTBAR_SLOT_COUNT = 9;

	/**
	 * Container offset.
	 */
	static final int HOTBAR_XPOS = 8;

	/**
	 * Container offset.
	 */
	static final int HOTBAR_YPOS = 142;

	/**
	 * Container offset.
	 */
	static final int PLAYER_INVENTORY_XPOS = 8;

	/**
	 * Container offset.
	 */
	static final int PLAYER_INVENTORY_YPOS = 84;

	/**
	 * Container offset.
	 */
	static final int PLAYER_INVENTORY_ROW_COUNT = 3;

	/**
	 * Container offset.
	 */
	static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

	/**
	 * Container offset.
	 */
	static final int COMPOSITE_SLOT_COUNT = COMPOSITE_MAX_SIZE;

	/**
	 * Container offset.
	 */
	static final int COMPOSITE_XPOS = 17;

	/**
	 * Container offset.
	 */
	static final int COMPOSITE_YPOS = 26;

	/**
	 * Item stack for the composite magic item.
	 */
	ItemStack itemStackBeingHeld;

	/**
	 * Player inventory
	 */
	PlayerInventory inventory;

	/**
	 * Composite item inventory.
	 */
	ItemStackHandler compositeItemInventory;

	/**
	 * Constructor.
	 * 
	 * The constructor is used client side.
	 * 
	 * On the client side there is no parent ItemStack to communicate with, so a
	 * dummy inventory is used.
	 * 
	 * The extra data from the server isn't used.
	 * 
	 * @param id        window ID.
	 * @param inventory player inventory.
	 * @param extraData extra data sent from the server.
	 */
	public CompositeMagicItemContainer(int id, PlayerInventory inventory, PacketBuffer extraData) {
		this(id, inventory, new CompositeMagicItemItemStackHandler(COMPOSITE_MAX_SIZE), ItemStack.EMPTY);
	}

	/**
	 * Constructor.
	 * 
	 * The constructor is used server side.
	 * 
	 * @param id                     window ID.
	 * @param inventory              player inventory.
	 * @param compositeItemInventory inventory for the composite magic item.
	 * @param compositeItem          item stack for the composite magic item.
	 */
	public CompositeMagicItemContainer(int id, PlayerInventory inventory, ItemStackHandler compositeItemInventory,
			ItemStack compositeItem) {
		super(COMPOSITE_ITEM_COMTAINER.get(), id);
		this.itemStackBeingHeld = compositeItem;
		this.inventory = inventory;
		this.compositeItemInventory = compositeItemInventory;

		addCompositeSlots();
		addHotBarSlots();
		addPlayerInventorySlots();
	}
	
	/**
	 * Add composite slots to GUI. Container slots are created with index at [0..5].
	 */
	void addCompositeSlots() {
		for (int x = 0; x < COMPOSITE_SLOT_COUNT; x++) {
			int index = x;
			addSlot(new SlotItemHandler(compositeItemInventory, index, COMPOSITE_XPOS + SLOT_X_SPACING * x,
					COMPOSITE_YPOS));
		}
	}

	/**
	 * Add hotbar slots to GUI. Container slots are created with index at [0..8].
	 */
	void addHotBarSlots() {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int index = x;
			addSlot(new Slot(inventory, index, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}
	}

	/**
	 * Add player inventory slots to GUI. Container slots are created with index at
	 * [9..35].
	 */
	void addPlayerInventorySlots() {
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int index = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlot(new Slot(inventory, index, xpos, ypos));
			}
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {

		// exit if at logical client side
		if (WorldUtils.isLogicalClient(player))
			return false;

		// get item in main hand
		ItemStack main = player.getHeldItemMainhand();

		// can't interact if main had is empty
		if (main.isEmpty())
			return false;

		return main == itemStackBeingHeld;
	}

}
