package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addAggroMobEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addAggroMobEffectDuration;
import static bassebombecraft.config.ModConfiguration.mobsAggroIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddAggroMobEffect;

/**
 * Mobs Aggro idol implementation.
 */
public class MobsAggroIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MobsAggroIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addAggroMobEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addAggroMobEffectAmplifier.get();

	public MobsAggroIdolInventoryItem() {
		super(ITEM_NAME, mobsAggroIdolInventoryItem, new AddAggroMobEffect(splDuration, splAmplifier));
	}
}
