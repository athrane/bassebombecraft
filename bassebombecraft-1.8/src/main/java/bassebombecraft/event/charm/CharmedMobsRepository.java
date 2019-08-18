package bassebombecraft.event.charm;

import java.util.Collection;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

/**
 * Interface for repository for handling charmed mobs.
 */
public interface CharmedMobsRepository {

	/**
	 * Charm mob.
	 * 
	 * @param entity    mob which is charmed.
	 * @param commander entity which charmed mob.
	 */
	public void add(MobEntity entity, LivingEntity commander);

	/**
	 * Remove mob.
	 * 
	 * @param entity mob which is removed.
	 */
	public void remove(MobEntity entity);

	/**
	 * Update charm.
	 * 
	 * If duration is expired then mob is removed.
	 * 
	 * @param entity mob which is charmed.
	 */
	public void update(MobEntity entity);

	/**
	 * Returns true if mob is already charmed.
	 * 
	 * @param entity mob to query.
	 * @return true if mob is already charmed.
	 */
	public boolean contains(MobEntity entity);

	/**
	 * Get charmed mobs. Iteration must be take place in synchronized section.
	 * 
	 * @return collection of charmed mobs
	 */
	public Collection<CharmedMob> get();

}
