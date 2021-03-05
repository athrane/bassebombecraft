package bassebombecraft.client.event.rendering.effect;

import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;

/**
 * Graphical effect.
 */
public interface GraphicalEffect {

	/**
	 * Return source entity.
	 * 
	 * @return source entity.
	 */
	public Entity getSource();

	/**
	 * Return target entity.
	 * 
	 * @return target entity.
	 */
	public Entity getTarget();

	/**
	 * Return remaining duration of effect (in game ticks).
	 * 
	 * @return remaining duration of effect (in game ticks).
	 */
	public int getDuration();

	/**
	 * Get effect operator.
	 * 
	 * @return effect operator.
	 */
	public Operator2 getEffectOperator();

	/**
	 * Return ID used to identify instance in repositories.
	 * 
	 * @return ID used to identify instance in repositories
	 */
	public String getId();
}
