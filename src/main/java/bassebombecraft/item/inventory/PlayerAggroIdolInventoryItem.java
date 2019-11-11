package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addPlayerAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addPlayerAggroEffectDuration;

import java.util.function.Supplier;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.action.inventory.AddPlayerAggroEffect;

/**
 * Player Aggro idol implementation.
 */
public class PlayerAggroIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PlayerAggroIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addPlayerAggroEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addPlayerAggroEffectAmplifier.get();

	public PlayerAggroIdolInventoryItem() {
		super(ITEM_NAME, ModConfiguration.playerAggroIdolInventoryItem,
				new AddPlayerAggroEffect(splDuration, splAmplifier));
	}
}
