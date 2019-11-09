package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addLevitationEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addLevitationEffectDuration;
import static bassebombecraft.config.ModConfiguration.levitationIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddLevitationEffect;

/**
 * Levitation idol implementation.
 */
public class LevitationIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = LevitationIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addLevitationEffectDuration.get();
	static Supplier<Integer> splAmplification = () -> addLevitationEffectAmplifier.get();

	public LevitationIdolInventoryItem() {
		super(ITEM_NAME, levitationIdolInventoryItem, new AddLevitationEffect(splDuration, splAmplification));
	}
}
