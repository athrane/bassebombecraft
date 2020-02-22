package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addFlameEffectDuration;
import static bassebombecraft.config.ModConfiguration.flameBlastIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddFlameEffect;

/**
 * Flameblast Dragon idol implementation.
 */
public class FlameBlastIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = FlameBlastIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addFlameEffectDuration.get();

	public FlameBlastIdolInventoryItem() {
		super(ITEM_NAME, flameBlastIdolInventoryItem, new AddFlameEffect(splDuration));
	}
}
