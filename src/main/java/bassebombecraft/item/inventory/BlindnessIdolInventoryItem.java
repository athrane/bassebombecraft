package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addBlindingEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addBlindingEffectDuration;
import static bassebombecraft.config.ModConfiguration.blindnessIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddBlindingEffect;

/**
 * Blindness idol implementation.
 */
public class BlindnessIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = BlindnessIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addBlindingEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addBlindingEffectAmplifier.get();
	
	public BlindnessIdolInventoryItem() {
		super(ITEM_NAME, blindnessIdolInventoryItem, new AddBlindingEffect(splDuration, splAmplifier));
	}
}
