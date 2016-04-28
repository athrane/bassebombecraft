package bassebombecraft.item.action.mist.entity;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

/**
 * Strategy for entity mist action.
 */
public interface EntityMistActionStrategy {

	/**
	 * Apply effect to target entity.
	 * 
	 * @param target
	 *            target entity.
	 * @param MistPos
	 *            position of the mist.
	 */
	void applyEffectToEntity(EntityLivingBase target, Vec3 mistPos);

	/**
	 * Return effect duration.
	 * 
	 * @return effect duration.
	 */
	int getEffectDuration();

	/**
	 * Returns true if effect is applied to invoker.
	 * 
	 * @return true if effect is applied to invoker.
	 */
	boolean isEffectAppliedToInvoker();

	/**
	 * Returns true if mist is stationary, otherwise is will move away from the
	 * player.
	 * 
	 * @return true if mist is stationary.
	 */
	boolean isStationary();

	/**
	 * Return true if mist is disabled when effect has been applied once.
	 * 
	 * @return true if mist is disabled when effect has been applied once.
	 */
	boolean isOneShootEffect();

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
