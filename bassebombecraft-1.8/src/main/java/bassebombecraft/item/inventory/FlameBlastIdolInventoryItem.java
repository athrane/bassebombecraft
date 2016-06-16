package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddFlameEffect;

/**
 * Flameblast Dragon idol implementation.
 */
public class FlameBlastIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = FlameBlastIdolInventoryItem.class.getSimpleName();

	public FlameBlastIdolInventoryItem() {
		super(ITEM_NAME, new AddFlameEffect());
	}
}
