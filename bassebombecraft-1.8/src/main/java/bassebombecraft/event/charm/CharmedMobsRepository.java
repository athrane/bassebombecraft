package bassebombecraft.event.charm;

import java.util.stream.Stream;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for repository for handling charmed mobs.
 */
public interface CharmedMobsRepository {

	/**
	 * Charm mob.
	 * 
	 * @param entity
	 *            mob which is charmed.
	 * @param commander
	 *            entity which charmed mob.
	 */
	public void add(EntityLiving entity, EntityLivingBase commander);

	/**
	 * Remove mob.
	 * 
	 * @param entity
	 *            mob which is removed.
	 */
	public void remove(EntityLiving entity);

	/**
	 * Update charm.
	 * 
	 * If duration is expired then mob is removed.
	 * 
	 * @param entity
	 *            mob which is charmed.
	 */
	public void update(EntityLiving entity);

	/**
	 * Returns true if mob is already charmed.
	 * 
	 * @param entity
	 *            mob to query.
	 * @return true if mob is already charmed.
	 */
	public boolean contains(EntityLiving entity);

	/**
	 * Get stream of charmed mobs.
	 * 
	 * @return stream of charmed mobs
	 */
	public Stream<CharmedMob> get();

	/**
	 * Refresh repository. Removes any dead entities.
	 */
	public void removeDeadEntities();

}
