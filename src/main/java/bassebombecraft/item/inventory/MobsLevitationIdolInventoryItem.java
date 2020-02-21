package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.addMobsLevitationEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addMobsLevitationEffectDuration;
import static bassebombecraft.config.ModConfiguration.mobsLevitationIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.AddMobsLevitationEffect;

/**
 * Mobs levitation idol implementation.
 */
public class MobsLevitationIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MobsLevitationIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDuration = () -> addMobsLevitationEffectDuration.get();
	static Supplier<Integer> splAmplifier = () -> addMobsLevitationEffectAmplifier.get();

	public MobsLevitationIdolInventoryItem() {
		super(ITEM_NAME, mobsLevitationIdolInventoryItem,
				new AddMobsLevitationEffect(splDuration, splAmplifier));
	}

}
