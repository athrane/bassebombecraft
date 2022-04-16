package bassebombecraft.entity.commander.command;

import java.util.Collections;
import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import bassebombecraft.util.function.DiscardTeamMembers;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

/**
 * Attack nearest mob command.
 */
public class AttackNearestMobCommand implements MobCommand {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AttackNearestMobCommand.class.getSimpleName();

	/**
	 * First list index.
	 */
	static final int FIRST_INDEX = 0;

	/**
	 * Target distance.
	 */
	final int targetDistance;

	/**
	 * Entity distance sorter.
	 */
	EntityDistanceSorter entityDistanceSorter = new EntityDistanceSorter();

	/**
	 * Discard team members filter.
	 */
	DiscardTeamMembers discardMembersFilter = new DiscardTeamMembers();

	/**
	 * AttackNearestMobCommand constructor.
	 */
	public AttackNearestMobCommand() {
		targetDistance = ModConfiguration.attackNearestMobCommandTargetDistance.get();
	}

	@Override
	public Commands getType() {
		return Commands.NEAREST_MOB;
	}

	@Override
	public String getTitle() {
		return "Attack nearest mob";
	}

	@Override
	public boolean shouldExecute(LivingEntity commander, PathfinderMob entity) {

		// initialize filter
		discardMembersFilter.set(entity);

		// get list of mobs
		AABB aabb = entity.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
		List<Mob> targetList = entity.level.getEntitiesOfClass(Mob.class, aabb, discardMembersFilter);

		// exit if no targets where found
		if (targetList.isEmpty())
			return false;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get target
		Mob target = targetList.get(FIRST_INDEX);

		// update target
		entity.setTarget(target);

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target is dead
		if (!target.isAlive())
			return false;

		return true;
	}

	@Override
	public void tick(LivingEntity commander, PathfinderMob entity) {
		// NO-OP
	}

}
