package bassebombecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

/**
 * Interface for creation AI for entities.
 */
public interface EntityAiBuilder {

	/**
	 * Build AI for living entity. 
	 * 
	 * @param entity entity to build AI for.
	 */
	void build(EntityLiving entity);
}
