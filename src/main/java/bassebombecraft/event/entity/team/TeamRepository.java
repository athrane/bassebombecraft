package bassebombecraft.event.entity.team;

import java.util.Collection;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for repository for handling team memberships.
 */
public interface TeamRepository {

	/**
	 * Create team.
	 * 
	 * @param commander commander of the team.
	 */
	public void createTeam(PlayerEntity commander);

	/**
	 * Returns true if entity is commander for a team.
	 * 
	 * @param commander candidate.
	 * 
	 * @return true if entity is commander for a team.
	 */
	public boolean isCommander(PlayerEntity commander);

	/**
	 * Delete commanders team.
	 * 
	 * @param commander whose team should be deleted.
	 */
	public void deleteTeam(PlayerEntity commander);

	/**
	 * Get team size for commander.
	 * 
	 * @param commander commander to get team size for.
	 * 
	 * @return size of commanders team.
	 */
	public int size(PlayerEntity commander);

	/**
	 * Add team member.
	 * 
	 * If creator isn't a player then the entity is added to the team to which the
	 * creator is member if.
	 * 
	 * @param creator creator of entity to register as team member.
	 * @param entity  entity which is added to team.
	 */
	public void add(LivingEntity creator, LivingEntity entity);

	/**
	 * Remove team member from any team.
	 * 
	 * @param entity entity which is removed from team.
	 */
	public void remove(LivingEntity entity);

	/**
	 * Returns true if entity is part of team.
	 * 
	 * @param entity   entity to query for membership.
	 * @param teamName name of team to add entity to.
	 * 
	 * @return true if entity is member of team.
	 */
	public boolean isMember(LivingEntity commander, LivingEntity entity);

	/**
	 * Returns true if entities are members of the same team.
	 * 
	 * @param entity  entity to query for membership.
	 * @param entity2 entity to query for membership.
	 * 
	 * @return true if entity is member of team.
	 */
	public boolean isTeamMembers(LivingEntity entity, LivingEntity entity2);

	/**
	 * Get team members. Iteration must be take place in synchronized section.
	 * 
	 * @return collection of team members.
	 */
	public Collection<LivingEntity> get(PlayerEntity commander);

}
