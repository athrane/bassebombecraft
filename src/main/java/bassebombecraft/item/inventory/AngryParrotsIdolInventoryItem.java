package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.angryParrotsIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.*;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.SpawnAngryParrots;

/**
 * Angry parrots idol implementation.
 */
public class AngryParrotsIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = AngryParrotsIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDamage = () -> spawnAngryParrotsDamage.get();
	static Supplier<Double> splMovementSpeed = () -> spawnAngryParrotsMovementSpeed.get();
	
	public AngryParrotsIdolInventoryItem() {
		super(ITEM_NAME, angryParrotsIdolInventoryItem, new SpawnAngryParrots(splDamage, splMovementSpeed));
	}
}
