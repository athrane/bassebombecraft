package bassebombecraft.event.entity.target;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Interface for repository for handling targeted entities.
 */
public interface TargetRepository {

	/**
	 * Create target set for commander.
	 * 
	 * @param commander commander.
	 */
	public void createTargets(Player commander);

	/**
	 * Delete commanders targets.
	 * 
	 * @param commander whose targets should be deleted.
	 */
	public void deleteTargets(Player commander);

	/**
	 * Clear all targets for commander.
	 * 
	 * @param commander commander.
	 */
	public void clear(Player commander);
	
	/**
	 * Returns true if entity is commander for a team.
	 * 
	 * @param commander candidate.
	 * 
	 * @return true if entity is commander for a team.
	 */
	public boolean isCommander(Player commander);
	
	/**
	 * Add targeted entity.
	 * 
	 * @param commander to whose target list the target should be added.
	 * @param entity entity which is targeted.
	 */
	public void add(Player commander, LivingEntity entity);

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
	public Stream<LivingEntity> get(Player commander);

	/**
	 * Get the commanders first prioritised target.
	 * 
	 * @param commander commander to get target for.
	 */
	public Optional<LivingEntity> getFirst(Player commander);

	/**
	 * Get the commanders first prioritised target.
	 * 
	 * @param commander commander to get target for.
	 */
	public Optional<LivingEntity> getFirst(LivingEntity commander);
	
	/**
	 * Get number of targeted entities by commander.
	 * 
	 * @param commander commander to get number of targets for.
	 * 
	 * @return number of targeted entities by commander.
	 */
	public int size(Player commander);

}
