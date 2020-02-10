package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addHealingEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addHealingEffectDuration;
import static bassebombecraft.config.ModConfiguration.reflectIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddReflectEffect;

/**
 * Revenge idol implementation.
 */
public class ReflectIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = ReflectIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addHealingEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addHealingEffectAmplifier.get();

	public ReflectIdolInventoryItem() {
		super(ITEM_NAME, reflectIdolInventoryItem, new AddReflectEffect(splDuration, splAmplifier));
	}
}
