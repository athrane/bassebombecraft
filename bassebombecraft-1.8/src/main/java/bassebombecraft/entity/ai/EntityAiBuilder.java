package bassebombecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for creation AI for entities.
 */
@Deprecated
public interface EntityAiBuilder {

	/**
	 * Build AI for living entity.
	 * 
	 * @param entity
	 *            entity to build AI for.
	 */
	void build(EntityLiving entity);

	/**
	 * Build AI for living entity.
	 * 
	 * @param entity
	 *            entity to build AI for.
	 * @param owner
	 *            entity which controls entity.
	 */
	void build(EntityLiving entity, EntityLivingBase owner);
}
