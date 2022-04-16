package bassebombecraft.event.charm;

import java.util.stream.Stream;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.proxy.Proxy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * Interface for repository for handling charmed mobs.
 * 
 * The repository is used at both SERVER and CLIENT side. Access to the
 * repository is supported via sided proxy, i.e.{@linkplain Proxy}.
 * 
 * The SERVER side implementation {@linkplain ServerCharmedMobsRepository} will
 * implement the actual charm logic . The CLIENT side implementation
 * {@linkplain ClientCharmedMobsRepository }will only implement logic to
 * support rendering.
 */
public interface CharmedMobsRepository {

	/**
	 * Charm mob.
	 * 
	 * @param entity    mob which is charmed.
	 * @param commander entity which charmed mob.
	 */
	public void add(Mob entity, LivingEntity commander);

	/**
	 * Remove mob.
	 * 
	 * @param id ID of entity which is removed. The ID is read from
	 *           {@linkplain Entity.getEntityId()}.
	 */
	public void remove(String id);

	/**
	 * Remove mob.
	 * 
	 * @param entity entity to remove.
	 */
	public void remove(Mob entity);

	/**
	 * Returns true if mob is already charmed.
	 * 
	 * @param id ID of entity to query for. The ID is read from
	 *           {@linkplain Entity.getEntityId()}.
	 * @return true if mob is already charmed.
	 */
	public boolean contains(String id);

	/**
	 * Returns true if mob is already charmed.
	 * 
	 * @param entity entity to query for.
	 * 
	 * @return true if mob is already charmed.
	 */
	public boolean contains(Mob entity);

	/**
	 * Get charmed mobs.
	 * 
	 * @return stream of charmed mobs..
	 */
	public Stream<CharmedMob> get();

	/**
	 * Get number of mobs charmed.
	 * 
	 * @return number of mobs charmed.
	 */
	public int size();
}
