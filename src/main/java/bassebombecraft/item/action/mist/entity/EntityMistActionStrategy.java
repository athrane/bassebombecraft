package bassebombecraft.item.action.mist.entity;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

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
	 * @param invoker
	 *            invoker entity.
	 */
	void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker);

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
	 * Get rendering info used to render mist.
	 * 
	 * @return rendering info used to render mist.
	 */
	ParticleRenderingInfo getRenderingInfos();

}
