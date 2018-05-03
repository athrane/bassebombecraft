package bassebombecraft.item;

import bassebombecraft.BassebombeCraft;
import net.minecraft.item.Item;

/**
 * Utility for items.
 */
public class ItemUtils {

	/**
	 * Do common initialisation of item.
	 * 
	 * @param item
	 *            item to initialise.
	 * @param name
	 *            item name.
	 */
	public static void initializeItem(Item item, String name) {
		item.setUnlocalizedName(name);
		item.setRegistryName(name);
		item.setCreativeTab(BassebombeCraft.getCreativeTab());
	}
}
