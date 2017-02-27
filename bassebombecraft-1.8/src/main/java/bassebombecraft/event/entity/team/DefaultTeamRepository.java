package bassebombecraft.event.entity.team;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.EntityLivingBase;

/**
 * Default implementation of the {@linkplain TeamRepository}.
 */
public class DefaultTeamRepository implements TeamRepository {

	/**
	 * DTO for team repository.
	 */
	class Team {

		/**
		 * Team constructor.
		 * 
		 * @param teamName
		 *            team name.
		 */
		public Team(String teamName) {
			this.name = teamName;
		}

		/**
		 * Team name.
		 */
		String name;

		/**
		 * Team members.
		 */
		Set<EntityLivingBase> members = new HashSet<EntityLivingBase>();

		@Override
		public boolean equals(Object obj) {
			Team team = (Team) obj;
			return this.name.equalsIgnoreCase(team.name);
		}

	}

	/**
	 * Teams.
	 */
	Map<String, Team> teams = new ConcurrentHashMap<String, Team>();

	/**
	 * Entity to team mapping.
	 */
	Map<EntityLivingBase, Team> teamMembership = new ConcurrentHashMap<EntityLivingBase, Team>();

	@Override
	public void createTeam(String teamName) {
		if (teamName == null)
			return;
		if (teamExists(teamName))
			return;

		teams.put(teamName, new Team(teamName));		
	}

	@Override
	public void add(EntityLivingBase entity, String teamName) {
		if (entity == null)
			return;
		if (teamName == null)
			return;
		
		// create team if it doesn't exit
		if (!teamExists(teamName))
			createTeam(teamName);

		// get team
		Team team = teams.get(teamName);

		// add to global membership list
		teamMembership.put(entity, team);

		// add to team
		team.members.add(entity);
		
	}

	@Override
	public boolean teamExists(String teamName) {
		if (teamName == null)
			return false;
		return teams.containsKey(teamName);
	}

	@Override
	public void remove(EntityLivingBase entity) {
		if (entity == null)
			return;
		if (!teamMembership.containsKey(entity))
			return;

		// get team
		Team team = teamMembership.get(entity);

		// remove from team
		team.members.remove(entity);

		// remove from global membership list
		teamMembership.remove(entity);
	}

	@Override
	public boolean contains(EntityLivingBase entity, String teamName) {
		if (entity == null)
			return false;
		if (teamName == null)
			return false;

		Team team = teams.get(teamName);
		return team.members.contains(entity);
	}

	@Override
	public boolean isTeamMembers(EntityLivingBase entity, EntityLivingBase entity2) {
		if (entity == null)
			return false;
		if (entity2 == null)
			return false;

		if (!teamMembership.containsKey(entity))
			return false;
		if (!teamMembership.containsKey(entity2))
			return false;

		// get teams
		Team team = teamMembership.get(entity);
		Team team2 = teamMembership.get(entity2);

		// compare
		return team.equals(team2);
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static TeamRepository getInstance() {
		return new DefaultTeamRepository();
	}

}
