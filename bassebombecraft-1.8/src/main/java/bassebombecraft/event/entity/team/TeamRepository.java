package bassebombecraft.event.entity.team;

import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for repository for handling team memberships.
 */
public interface TeamRepository {

	/**
	 * Create team.
	 * 
	 * @param teamName
	 *            name of team to create.
	 */
	public void createTeam(String teamName);

	/**
	 * Returns true if team exists.
	 * @param teamName true if team exists.
	 * @return true if team exists.
	 */
	public boolean teamExists(String teamName);
	
	/**
	 * Add team member.
	 * 
	 * @param entity
	 *            entity which is added to team.
	 * @param teamName
	 *            name of team to add entity to.
	 */
	public void add(EntityLivingBase entity, String teamName);

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
	public boolean contains(EntityLivingBase entity, String teamName);

	/**
	 * Returns true if entities are members of the same team.
	 * 
	 * @param entity
	 *            entity one
	 * @param entity2
	 *            entity two
	 * @return true if entities are members of the same team.
	 */
	public boolean isTeamMembers(EntityLivingBase entity, EntityLivingBase entity2);

}
