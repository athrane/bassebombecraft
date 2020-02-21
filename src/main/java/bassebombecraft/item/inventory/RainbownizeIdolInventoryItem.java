package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.rainbownizeIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.rainbownizeSpiralSize;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.Rainbownize;

/**
 * Rainbownize idol implementation.
 */
public class RainbownizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = RainbownizeIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splSpiralSize = () -> rainbownizeSpiralSize.get();

	public RainbownizeIdolInventoryItem() {
		super(ITEM_NAME, rainbownizeIdolInventoryItem, new Rainbownize(splSpiralSize));
	}
}
