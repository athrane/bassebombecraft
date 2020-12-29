package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.flameBlastIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddFlameEffect;

/**
 * Flameblast Dragon idol implementation.
 */
public class FlameBlastIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = FlameBlastIdolInventoryItem.class.getSimpleName();

	public FlameBlastIdolInventoryItem() {
		super(flameBlastIdolInventoryItem, new AddFlameEffect());
	}
}
