package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.*;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddSaturationEffect;

/**
 * Saturation idol implementation.
 */
public class SaturationIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = SaturationIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addSaturationEffectDuration.get();
	
	public SaturationIdolInventoryItem() {
		super(ITEM_NAME, saturationIdolInventoryItem, new AddSaturationEffect(splDuration));
	}
}
