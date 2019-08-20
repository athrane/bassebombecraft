package bassebombecraft.item;

import bassebombecraft.item.inventory.GenericInventoryItem;
import net.minecraft.item.Item;

/**
 * Utility for items.
 */
public class ItemUtils {

	/**
	 * Do common initialisation of item.
	 * 
	 * Name is stored with lower case in the item registry.
	 * 
	 * @param item item which is initialised..
	 * @param name item name.
	 */
	public static void doCommonItemInitialization(Item item, String name) {
		item.setRegistryName(name.toLowerCase());
	}

	/**
	 * Returns true if object is a subclass of the
	 * {@linkplain GenericInventoryItem}.
	 * 
	 * @param object to test.
	 * @return true if object is a subclass of the
	 *         {@linkplain GenericInventoryItem}.
	 */
	public static boolean isInventoryItem(Object object) {
		Class<? extends Object> superClass = GenericInventoryItem.class;
		return superClass.isAssignableFrom(object.getClass());
	}
}
