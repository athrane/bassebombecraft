package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.eggProjectileIdolInventoryItem;

import bassebombecraft.item.action.inventory.ShootEgg;

/**
 * Egg-Terminator implementation.
 */
public class EggProjectileIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = EggProjectileIdolInventoryItem.class.getSimpleName();

	public EggProjectileIdolInventoryItem() {
		super(eggProjectileIdolInventoryItem, new ShootEgg());
	}
}
