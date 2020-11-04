package bassebombecraft.item;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.item.inventory.GenericInventoryItem;

/**
 * Utility for items.
 */
public class ItemUtils {

	/**
	 * Returns true if object is a subclass of the
	 * {@linkplain GenericInventoryItem}.
	 * 
	 * @param object to test.
	 * 
	 * @return true if object is a subclass of the
	 *         {@linkplain GenericInventoryItem}.
	 */
	public static boolean isTypeInventoryItem(Object object) {
		Class<? extends Object> superClass = GenericInventoryItem.class;
		return superClass.isAssignableFrom(object.getClass());
	}

	/**
	 * Returns true if object is a subclass of the
	 * {@linkplain GenericCompositeNullItem}.
	 * 
	 * @param object to test.
	 * 
	 * @return true if object is a subclass of the
	 *         {@linkplain GenericCompositeNullItem}.
	 */
	public static boolean isTypeCompositeItem(Object object) {
		Class<? extends Object> superClass = GenericCompositeNullItem.class;
		return superClass.isAssignableFrom(object.getClass());
	}

}
