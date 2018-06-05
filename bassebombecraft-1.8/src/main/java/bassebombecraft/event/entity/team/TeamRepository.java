package bassebombecraft.event.entity.team;

import java.util.stream.Stream;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for repository for handling team memberships.
 */
public interface TeamRepository {

	/**
	 * Create team.
	 * 
	 * @param commander
	 *            commander of the team.
	 */	
	public void createTeam(EntityPlayer commander);
	
	/**
	 * Returns true if team exists.
	 * 
	 * @param commander true if team with commander.
	 * 
	 * @return true if team exists.
	 */
	public boolean teamExists(EntityPlayer commander);
	
	/**
	 * Add team member.
	 * 
	 * @param commander
	 *            commander of the team.
	 * @param entity
	 *            entity which is added to team.
	 */
	public void add(EntityPlayer commander, EntityLivingBase entity);

	/**
	 * Add team member.
	 * 
	 * @param creator
	 *            creator of entity to register as team member.
	 * @param entity
	 *            entity which is added to team.
	 */
	public void add(EntityLivingBase creator, EntityLivingBase entity);
	
	/**
	 * Remove team member from any team.
	 * 
	 * @param entity
	 *            entity which is removed from team.
	 */
	public void remove(EntityLivingBase entity);

	/**
	 * Returns true if entity is part of team.
	 * 
	 * @param entity
	 *            entity to query for membership.
	 * @param teamName
	 *            name of team to add entity to.
	 * 
	 * @return true if entity is member of team.
	 */
	public boolean isMember(EntityPlayer commander, EntityLivingBase entity);

	/**
	 * Returns true if entities are members of the same team.
	 * 
	 * @param entity
	 *            entity to query for membership.
	 * @param entity2
	 *            entity to query for membership.
	 * 
	 * @return true if entity is member of team.
	 */	
	public boolean isTeamMembers(EntityLivingBase entity, EntityLivingBase entity2);

	/**
	 * Get stream of team members.
	 * 
	 * @return stream of team members.
	 */
	public Stream<EntityLivingBase> get(EntityPlayer commander);
	
}
