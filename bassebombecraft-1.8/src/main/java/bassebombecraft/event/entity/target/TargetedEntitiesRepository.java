package bassebombecraft.event.entity.target;

import java.util.stream.Stream;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for repository for handling targeted entities.
 */
public interface TargetedEntitiesRepository {
	
	/**
	 * Add targeted entity.
	 * 
	 * @param entity
	 *            entity which is targeted.
	 */
	public void add(EntityLivingBase entity);
	
	/**
	 * Remove team member from any team.
	 * 
	 * @param entity
	 *            entity which is removed as targeted.
	 */
	public void remove(EntityLivingBase entity);

	/**
	 * Get stream of targeted entities.
	 * 
	 * @return stream of targeted entities.
	 */
	public Stream<EntityLivingBase> get(EntityPlayer commander);
	
}
