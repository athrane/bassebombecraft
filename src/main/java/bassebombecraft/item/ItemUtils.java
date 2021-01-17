package bassebombecraft.item;

import static bassebombecraft.ModConstants.PFM_SUFFIX;
import static bassebombecraft.ModConstants.PF_SUFFIX;
import static bassebombecraft.ModConstants.PM_SUFFIX;
import static bassebombecraft.ModConstants.PP_SUFFIX;
import static bassebombecraft.ModConstants.P_SUFFIX;
import static bassebombecraft.ModConstants.UNKNOWN_SUFFIX;

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
		if (object == null)
			return false;
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
		if (object == null)
			return false;
		Class<? extends Object> superClass = GenericCompositeNullItem.class;
		return superClass.isAssignableFrom(object.getClass());
	}

	/**
	 * Resolve the name of the "type" from the class name of an composite item, i.e.
	 * a subclass of the {@linkplain GenericCompositeNullItem}.
	 * 
	 * The "type" resolved from the class name suffix.
	 * 
	 * @param item to resolve name from.
	 * 
	 * @return resolve type name.
	 */
	public static String resolveCompositeItemTypeFromString(GenericCompositeNullItem item) {
		String name = item.getClass().getSimpleName();
		if (name.endsWith(PF_SUFFIX))
			return "Projectile Formation";
		if (name.endsWith(PFM_SUFFIX))
			return "Projectile Formation Modifier";
		if (name.endsWith(P_SUFFIX))
			return "Projectile";
		if (name.endsWith(PP_SUFFIX))
			return "Projectile Path";
		if (name.endsWith(P_SUFFIX))
			return "Projectile Modifier";
		return UNKNOWN_SUFFIX;
	}

}
