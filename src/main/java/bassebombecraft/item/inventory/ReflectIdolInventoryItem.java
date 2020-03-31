package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addReflectEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addReflectEffectDuration;
import static bassebombecraft.config.ModConfiguration.reflectIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddReflectEffect;

/**
 * Reflect idol implementation.
 */
public class ReflectIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = ReflectIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addReflectEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addReflectEffectAmplifier.get();

	public ReflectIdolInventoryItem() {
		super(ITEM_NAME, reflectIdolInventoryItem, new AddReflectEffect(splDuration, splAmplifier));
	}
}
