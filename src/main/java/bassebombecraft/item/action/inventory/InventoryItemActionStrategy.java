package bassebombecraft.item.action.inventory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Strategy for inventory item action.
 */
public interface InventoryItemActionStrategy {

	/**
	 * Return true if effect only should be applied if item is selected in hotbar.
	 * 
	 * @return true if effect only should be applied if item is selected in hotbar.
	 */
	boolean applyOnlyIfSelected();

	/**
	 * Returns true if effect should be applied to target entity. This method is
	 * used to control whether the effect is applied to target, using the
	 * applyEffect method.
	 * 
	 * @param target          target entity.
	 * @param targetIsInvoker is true if the target is the invoker.
	 * 
	 * @return true if effect should be applied to target entity
	 */
	boolean shouldApplyEffect(Entity target, boolean targetIsInvoker);

	/**
	 * Apply effect to target entity.
	 * 
	 * @param target  target entity.
	 * @param world   world object.
	 * @param invoker invoker entity.
	 */
	void applyEffect(LivingEntity target, Level world, LivingEntity invoker);

}
