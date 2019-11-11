package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addHealingEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addHealingEffectDuration;
import static bassebombecraft.config.ModConfiguration.angelIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddHealingEffect;

/**
 * Angel idol implementation.
 */
public class AngelIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = AngelIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addHealingEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addHealingEffectAmplifier.get();

	public AngelIdolInventoryItem() {
		super(ITEM_NAME, angelIdolInventoryItem, new AddHealingEffect(splDuration, splAmplifier));
	}
}
