package bassebombecraft.event.entity.target;

import static bassebombecraft.entity.EntityUtils.getNullableTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class DefaultTargetedEntitiesRepository implements TargetedEntitiesRepository {

	/**
	 * DTO for targets repository, commanders set of targets.
	 */
	class Targets {

		/**
		 * Targets constructor.
		 * 
		 * @param team commander.
		 */
		public Targets(PlayerEntity commander) {
			this.commander = commander;
		}

		/**
		 * Team commander.
		 */
		PlayerEntity commander;

		/**
		 * Team targets.
		 */
		Set<LivingEntity> targets = Collections.synchronizedSet(new HashSet<LivingEntity>());

		@Override
		public boolean equals(Object obj) {
			Targets team = (Targets) obj;
			return this.commander.equals(team.commander);
		}

		@Override
		public String toString() {
			return "Targets for: " + commander.getGameProfile().getName();
		}

	}

	/**
	 * Null targets stream.
	 */
	final static Set<LivingEntity> nullTargetsSet = new HashSet<LivingEntity>();

	/**
	 * Set of commanders targets.
	 */
	Map<PlayerEntity, Targets> targetSets = new ConcurrentHashMap<PlayerEntity, Targets>();

	/**
	 * Entity-to-target-set mapping.
	 */
	Map<LivingEntity, Targets> targetMembership = new ConcurrentHashMap<LivingEntity, Targets>();

	@Override
	public void createTargets(PlayerEntity commander) {
		if (commander == null)
			return;
		if (isCommander(commander))
			return;

		// add commanders targets
		Targets commandersTargets = new Targets(commander);
		targetSets.put(commander, commandersTargets);
	}

	@Override
	public void deleteTargets(PlayerEntity commander) {
		if (commander == null)
			return;
		if (!isCommander(commander))
			return;

		// clear
		clear(commander);

		// remove commanders targets
		targetSets.remove(commander);
	}

	@Override
	public void clear(PlayerEntity commander) {
		if (commander == null)
			return;
		if (!isCommander(commander))
			return;

		// clear commanders targets from mapping
		Targets commandersTargets = targetSets.get(commander);
		Set<LivingEntity> targetSet = commandersTargets.targets;
		targetSet.forEach(t -> targetMembership.remove(t));
	}

	@Override
	public boolean isCommander(PlayerEntity commander) {
		if (commander == null)
			return false;
		return targetSets.containsKey(commander);
	}

	@Override
	public void add(PlayerEntity commander, LivingEntity entity) {
		if (commander == null)
			return;
		if (entity == null)
			return;

		// create commanders targets if it doesn't exit
		if (!isCommander(commander))
			createTargets(commander);

		// get commanders targets
		Targets commandersTargets = targetSets.get(commander);

		// add mapping to global membership list
		targetMembership.put(entity, commandersTargets);

		// add target to commanders targets
		commandersTargets.targets.add(entity);
	}

	@Override
	public void remove(LivingEntity entity) {
		if (entity == null)
			return;
		if (!targetMembership.containsKey(entity))
			return;

		// get commanders targets
		Targets commandersTargets = targetMembership.get(entity);

		// remove target from target set
		commandersTargets.targets.remove(entity);

		// remove mapping from global membership list
		targetMembership.remove(entity);
	}

	@Override
	public Stream<LivingEntity> get(PlayerEntity commander) {
		if (commander == null)
			return nullTargetsSet.stream();
		if (!isCommander(commander))
			return nullTargetsSet.stream();

		// get commanders targets
		Targets commandersTargets = targetSets.get(commander);

		return commandersTargets.targets.stream();
	}

	@Override
	public Optional<LivingEntity> getFirst(PlayerEntity commander) {
		if (commander == null)
			return empty();
		if (!isCommander(commander))
			return empty();

		// if commander has alive target then return it
		if (hasAliveTarget(commander)) {
			return getNullableTarget(commander);
		}

		// get commanders targets
		Targets commandersTargets = targetSets.get(commander);

		// if no targets are defined then return empty result
		if (commandersTargets.targets.isEmpty()) {
			return empty();
		}

		// return first result
		LivingEntity target = commandersTargets.targets.iterator().next();
		return ofNullable(target);
	}

	@Override
	public Optional<LivingEntity> getFirst(LivingEntity commander) {

		// exit if commander isn't a player
		if (!isTypePlayerEntity(commander))
			return empty();

		// type cast
		PlayerEntity commanderAsPlayer = (PlayerEntity) commander;

		return getFirst(commanderAsPlayer);
	}

	@Override
	public int size(PlayerEntity commander) {
		if (commander == null)
			return 0;
		if (!isCommander(commander))
			return 0;

		// get commanders targets
		Targets commandersTargets = targetSets.get(commander);

		return commandersTargets.targets.size();
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static TargetedEntitiesRepository getInstance() {
		return new DefaultTargetedEntitiesRepository();
	}

}
