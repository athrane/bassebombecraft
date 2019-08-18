package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Collections;
import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import bassebombecraft.predicate.DiscardTeamMembers;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Attack nearest mob command.
 */
public class AttackNearestMobCommand implements MobCommand {

	/**
	 * First list index.
	 */
	static final int FIRST_INDEX = 0;

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = AttackNearestMobCommand.class.getSimpleName();

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
		Config configuration = getBassebombeCraft().getConfiguration();
		targetDistance = configuration.getInt(CONFIG_KEY + ".TargetDistance");
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
	public boolean shouldExecute(LivingEntity commander, CreatureEntity entity) {

		// initialize filter
		discardMembersFilter.set(entity);

		// get list of mobs
		AxisAlignedBB aabb = entity.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
		List<MobEntity> targetList = entity.world.getEntitiesWithinAABB(MobEntity.class, aabb, discardMembersFilter);

		// exit if no targets where found
		if (targetList.isEmpty())
			return false;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get target
		MobEntity target = targetList.get(FIRST_INDEX);

		// update target
		entity.setAttackTarget(target);

		return true;
	}

	@Override
	public boolean continueExecuting(LivingEntity commander, CreatureEntity entity) {

		// get target
		LivingEntity target = entity.getAttackTarget();

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target is dead
		if (!target.isAlive())
			return false;

		return true;
	}

}
