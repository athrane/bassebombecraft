package bassebombecraft.event.entity.team;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

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
		 * @param team commander.
		 */
		public Team(PlayerEntity commander) {
			this.commander = commander;
		}

		/**
		 * Team commander.
		 */
		PlayerEntity commander;

		/**
		 * Team members.
		 */
		Set<LivingEntity> members = Collections.synchronizedSet(new HashSet<LivingEntity>());

		@Override
		public boolean equals(Object obj) {
			Team team = (Team) obj;
			return this.commander.equals(team.commander);
		}

		@Override
		public String toString() {
			return "Team: " + commander.getGameProfile().getName();
		}

	}

	/**
	 * Null member stream.
	 */
	final static Set<LivingEntity> nullMembersSet = new HashSet<LivingEntity>();

	/**
	 * Teams.
	 */
	Map<LivingEntity, Team> teams = new ConcurrentHashMap<LivingEntity, Team>();

	/**
	 * Entity to team mapping.
	 */
	Map<LivingEntity, Team> teamMembership = new ConcurrentHashMap<LivingEntity, Team>();

	@Override
	public void createTeam(PlayerEntity commander) {
		if (commander == null)
			return;
		if (isCommander(commander))
			return;

		Team team = new Team(commander);
		teams.put(commander, team);
	}

	@Override
	public void deleteTeam(PlayerEntity commander) {
		if (commander == null)
			return;
		if (!isCommander(commander))
			return;

		teams.remove(commander);
	}

	
	@Override
	public int size(PlayerEntity commander) {
		if (commander == null)
			return 0;
		if (!isCommander(commander))
			return 0;		
		
		// get team
		Team team = teams.get(commander);

		return team.members.size();
	}

	/**
	 * Add team member to commander's team.
	 * 
	 * @param commander commander to whose team the entity is added.
	 * @param entity    to add to commanders team.
	 */
	void addToCommandersTeam(PlayerEntity commander, LivingEntity entity) {

		// create team if it doesn't exit
		if (!isCommander(commander))
			createTeam(commander);

		// get team
		Team team = teams.get(commander);

		// add to global membership list
		teamMembership.put(entity, team);

		// add to team
		team.members.add(entity);
	}

	@Override
	public void add(LivingEntity creator, LivingEntity entity) {
		if (creator == null)
			return;
		if (entity == null)
			return;

		// if entity is player then add as commander
		if (PlayerUtils.isTypePlayerEntity(creator)) {
			PlayerEntity commander = (PlayerEntity) creator;
			addToCommandersTeam(commander, entity);
			return;
		}

		// exit if creator isn't a member of a team,
		// i.e. can be found in the global membership list
		if (!teamMembership.containsKey(creator))
			return;

		// get team that creator is member of
		Team team = teamMembership.get(creator);

		// get commander
		PlayerEntity commander = team.commander;

		// add to commanders team
		addToCommandersTeam(commander, entity);
	}

	@Override
	public boolean isCommander(PlayerEntity commander) {
		if (commander == null)
			return false;
		return teams.containsKey(commander);
	}

	@Override
	public void remove(LivingEntity entity) {
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
	public boolean isMember(LivingEntity commander, LivingEntity entity) {
		if (commander == null)
			return false;
		if (entity == null)
			return false;

		// get team
		Team team = teams.get(commander);

		if (team == null)
			return false;

		return team.members.contains(entity);
	}

	@Override
	public boolean isTeamMembers(LivingEntity entity, LivingEntity entity2) {
		if (entity == null)
			return false;
		if (entity2 == null)
			return false;

		// get teams
		Team team = teamMembership.get(entity);
		Team team2 = teamMembership.get(entity2);

		if (team == null)
			return false;
		if (team2 == null)
			return false;

		return team.equals(team2);
	}

	@Override
	public Collection<LivingEntity> get(PlayerEntity commander) {
		if (!isCommander(commander))
			return nullMembersSet;

		// get team
		Team team = teams.get(commander);
		return team.members;
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
