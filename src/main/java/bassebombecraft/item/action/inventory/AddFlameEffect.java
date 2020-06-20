package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addFlameEffectDuration;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spawns a entities within range om fire.
 */
public class AddFlameEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddFlameEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor
	 */
	public AddFlameEffect() {
		duration = addFlameEffectDuration.get();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(LivingEntity target, World world, LivingEntity sinvoker) {
		target.setFire(duration);
	}

}
