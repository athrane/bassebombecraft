package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addMobsPrimingEffectDuration;
import static bassebombecraft.config.ModConfiguration.primeMobIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddMobsPrimingEffect;

/**
 * Prime mob idol implementation.
 */
public class PrimeMobIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PrimeMobIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addMobsPrimingEffectDuration.get();

	public PrimeMobIdolInventoryItem() {
		super(ITEM_NAME, primeMobIdolInventoryItem, new AddMobsPrimingEffect(splDuration));
	}
}
