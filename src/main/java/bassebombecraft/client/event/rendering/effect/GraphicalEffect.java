package bassebombecraft.client.event.rendering.effect;

import net.minecraft.entity.LivingEntity;

/**
 * Graphical effect.
 */
public interface GraphicalEffect {
	
	/**
	 * Return source entity.
	 * 
	 * @return source entity.
	 */
	public LivingEntity getSource();

	/**
	 * Return target entity.
	 * 
	 * @return target entity.
	 */
	public LivingEntity getTarget();
	
	/**
	 * Return remaining duration of effect (in game ticks).
	 * 
	 * @return remaining duration of effect (in game ticks).
	 */
	public int getDuration();

}
