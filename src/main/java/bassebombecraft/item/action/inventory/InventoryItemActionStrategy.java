package bassebombecraft.item.action.inventory;

import javax.naming.OperationNotSupportedException;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

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
	void applyEffect(Entity target, World world, LivingEntity invoker);

	/**
	 * Get the area-of-effect range in blocks where the effect is triggered if an
	 * entity enters.
	 * 
	 * @return area-of-effect range in blocks where the effect is triggered if an
	 *         entity enters.
	 * 
	 * @throws OperationNotSupportedException migrated methods should throw this to
	 *                                        signal an error.
	 */
	@Deprecated
	int getEffectRange() throws OperationNotSupportedException;

	/**
	 * Get array of rendering info's used to render mist.
	 * 
	 * @return array of rendering info's used to render mist.
	 * 
	 * @throws OperationNotSupportedException migrated methods should throw this to
	 *                                        signal an error.
	 */
	@Deprecated
	ParticleRenderingInfo[] getRenderingInfos() throws OperationNotSupportedException;

}
