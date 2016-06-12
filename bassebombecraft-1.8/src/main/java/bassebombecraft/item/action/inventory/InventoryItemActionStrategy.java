package bassebombecraft.item.action.inventory;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Strategy for inventory item action.
 */
public interface InventoryItemActionStrategy {

	/**
	 * Returns true if effect should be applied to target entity. This method is
	 * used to control whether the effect is applied to target, using the
	 * applyEffect method.
	 * 
	 * @param target
	 *            target entity.
	 * @param targetIsInvoker
	 *            is true if the target is the invoker.
	 */
	boolean shouldApplyEffect(Entity target, boolean targetIsInvoker);

	/**
	 * Apply effect to target entity.
	 * 
	 * @param target
	 *            target entity.
	 * @param world
	 *            world object.
	 */
	void applyEffect(Entity target, World world);

	/**
	 * Get the area-of-effect range in blocks where the effect is triggered if
	 * an entity enters.
	 * 
	 * @return area-of-effect range in blocks where the effect is triggered if
	 *         an entity enters.
	 */
	int getEffectRange();

	/**
	 * Get array of rendering info's used to render mist.
	 * 
	 * @return array of rendering info's used to render mist.
	 */
	ParticleRenderingInfo[] getRenderingInfos();

}
