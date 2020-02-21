package bassebombecraft.item.action.inventory;

import java.util.function.Supplier;

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
	public final static String NAME = AddFlameEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * AddFlameEffect constructor
	 * 
	 * @param splDuration duration as a potion effect.
	 */
	public AddFlameEffect(Supplier<Integer> splDuration) {
		duration = splDuration.get();
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
	public void applyEffect(Entity target, World world, LivingEntity sinvoker) {
		target.setFire(duration);
	}

}
