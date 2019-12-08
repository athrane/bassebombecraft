package bassebombecraft.event.entity.target;

import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for repository for handling targeted entities.
 */
public interface TargetedEntitiesRepository {

	/**
	 * Add targeted entity.
	 * 
	 * @param entity entity which is targeted.
	 */
	public void add(LivingEntity entity);

	/**
	 * Remove target from any team.
	 * 
	 * @param entity entity which is removed as targeted.
	 */
	public void remove(LivingEntity entity);

	/**
	 * Get stream of targeted entities.
	 * 
	 * @param commander commander to get targeted entities for.
	 * 
	 * @return stream of targeted entities.
	 */
	public Stream<LivingEntity> get(PlayerEntity commander);

	/**
	 * Get number of targeted entities by commander.
	 * 
	 * @param commander commander to get number of targets for.
	 * 
	 * @return number of targeted entities by commander.
	 */
	public int size(PlayerEntity commander);

}
