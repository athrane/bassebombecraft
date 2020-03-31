package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addAggroPlayerEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addAggroPlayerEffectDuration;

import java.util.function.Supplier;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.action.inventory.AddAggroPlayerEffect;

/**
 * Player Aggro idol implementation.
 */
public class PlayerAggroIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = PlayerAggroIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addAggroPlayerEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addAggroPlayerEffectAmplifier.get();

	public PlayerAggroIdolInventoryItem() {
		super(ITEM_NAME, ModConfiguration.playerAggroIdolInventoryItem,
				new AddAggroPlayerEffect(splDuration, splAmplifier));
	}
}
