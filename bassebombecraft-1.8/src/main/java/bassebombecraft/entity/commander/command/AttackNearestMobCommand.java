package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Collections;
import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import bassebombecraft.predicate.DiscardTeamMembers;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
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
	EntityDistanceSorter entityDistanceSorter;

	/**
	 * Discard team members filter.
	 */
	DiscardTeamMembers discardMembersFilter;

	/**
	 * AttackNearestMobCommand constructor.
	 */
	public AttackNearestMobCommand() {
		entityDistanceSorter = new EntityDistanceSorter();
		discardMembersFilter = new DiscardTeamMembers();

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
	public boolean shouldExecute(EntityPlayer commander, EntityCreature entity) {

		// initialize filter
		discardMembersFilter.set(entity);

		// get list of mobs
		AxisAlignedBB aabb = entity.getEntityBoundingBox().expand(targetDistance, targetDistance, targetDistance);
		List<EntityMob> targetList = entity.world.getEntitiesWithinAABB(EntityMob.class, aabb, discardMembersFilter);

		// exit if no targets where found
		if (targetList.isEmpty())
			return false;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get target
		EntityMob target = targetList.get(FIRST_INDEX);

		// update target
		entity.setAttackTarget(target);

		return true;
	}

	@Override
	public boolean continueExecuting(EntityPlayer commander, EntityCreature entity) {

		// get target
		EntityLivingBase target = entity.getAttackTarget();

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target is dead
		if (target.isDead)
			return false;

		return true;
	}

}