package bassebombecraft.event.entity.team;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

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
		 * @param team
		 *            commander.
		 */
		public Team(EntityPlayer commander) {
			this.commander = commander;
		}

		/**
		 * Team commander.
		 */
		EntityPlayer commander;

		/**
		 * Team members.
		 */
		Set<EntityLiving> members = Collections.synchronizedSet(new HashSet<EntityLiving>());

		@Override
		public boolean equals(Object obj) {
			Team team = (Team) obj;
			return this.commander.equals(team.commander);
		}

	}

	/**
	 * Null member stream.
	 */
	final static Set<EntityLiving> nullMembersSet = new HashSet<EntityLiving>();

	/**
	 * Teams.
	 */
	Map<EntityLivingBase, Team> teams = new ConcurrentHashMap<EntityLivingBase, Team>();

	/**
	 * Entity to team mapping.
	 */
	Map<EntityLiving, Team> teamMembership = new ConcurrentHashMap<EntityLiving, Team>();

	@Override
	public void createTeam(EntityPlayer commander) {
		if (commander == null)
			return;
		if (teamExists(commander))
			return;

		Team team = new Team(commander);
		teams.put(commander, team);
	}

	/**
	 * Add team member to commander's team.
	 * 
	 * @param commander
	 *            commander to whose team the entity is added.
	 * @param entity
	 *            to add to commanders team.
	 */
	void addToCommandersTeam(EntityPlayer commander, EntityLiving entity) {
		if (commander == null)
			return;
		if (entity == null)
			return;

		// create team if it doesn't exit
		if (!teamExists(commander))
			createTeam(commander);

		// get team
		Team team = teams.get(commander);

		// add to global membership list
		teamMembership.put(entity, team);

		// add to team
		team.members.add(entity);
	}

	@Override
	public void add(EntityLivingBase creator, EntityLiving entity) {
		if (creator == null)
			return;
		if (entity == null)
			return;

		// if entity is player then add as commander
		if (PlayerUtils.isEntityPlayer(creator)) {
			EntityPlayer commander = (EntityPlayer) creator;
			addToCommandersTeam(commander, entity);
			return;
		}

		// exit if creator isn't member of global membership list
		if (!teamMembership.containsKey(creator))
			return;

		// get team
		Team team = teamMembership.get(creator);

		// get commander
		EntityPlayer commander = team.commander;

		// add to commanders team
		addToCommandersTeam(commander, entity);
	}

	@Override
	public boolean teamExists(EntityPlayer commander) {
		if (commander == null)
			return false;
		return teams.containsKey(commander);
	}

	@Override
	public void remove(EntityLiving entity) {
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
	public boolean isMember(EntityLivingBase commander, EntityLiving entity) {
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
	public boolean isTeamMembers(EntityLiving entity, EntityLiving entity2) {
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
	public Collection<EntityLiving> get(EntityPlayer commander) {
		if (!teamExists(commander))
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
