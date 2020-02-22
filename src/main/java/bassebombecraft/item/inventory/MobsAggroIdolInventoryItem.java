package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addMobsAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addMobsAggroEffectDuration;
import static bassebombecraft.config.ModConfiguration.mobsAggroIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddMobsAggroEffect;

/**
 * Mobs Aggro idol implementation.
 */
public class MobsAggroIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MobsAggroIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addMobsAggroEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addMobsAggroEffectAmplifier.get();

	public MobsAggroIdolInventoryItem() {
		super(ITEM_NAME, mobsAggroIdolInventoryItem, new AddMobsAggroEffect(splDuration, splAmplifier));
	}
}
