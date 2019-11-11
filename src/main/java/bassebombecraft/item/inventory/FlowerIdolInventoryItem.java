package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.flowerIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralSize;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.Naturalize;

/**
 * Flower idol implementation.
 */
public class FlowerIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = FlowerIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splSpiralSize = () -> naturalizeSpiralSize.get();

	public FlowerIdolInventoryItem() {
		super(ITEM_NAME, flowerIdolInventoryItem, new Naturalize(splSpiralSize));
	}
}
