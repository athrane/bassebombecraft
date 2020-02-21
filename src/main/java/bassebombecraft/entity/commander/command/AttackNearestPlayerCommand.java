package bassebombecraft.entity.commander.command;

import java.util.Collections;
import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import bassebombecraft.predicate.DiscardCommander;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Attack nearest player (except the team owner) command.
 */
public class AttackNearestPlayerCommand implements MobCommand {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AttackNearestPlayerCommand.class.getSimpleName();

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
	 * Discard team members filter used to discard commander.
	 */
	DiscardCommander discardTeamCommander = new DiscardCommander();

	/**
	 * AttackNearestMobCommand constructor.
	 */
	public AttackNearestPlayerCommand() {
		targetDistance = ModConfiguration.attackNearestPlayerCommandTargetDistance.get();
	}

	@Override
	public Commands getType() {
		return Commands.NEAREST_PLAYER;
	}

	@Override
	public String getTitle() {
		return "Attack nearest player";
	}

	@Override
	public boolean shouldExecute(LivingEntity commander, CreatureEntity entity) {

		// initialize filter
		discardTeamCommander.set(commander);

		// get list of mobs
		AxisAlignedBB aabb = entity.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
		List<PlayerEntity> targetList = entity.world.getEntitiesWithinAABB(PlayerEntity.class, aabb,
				discardTeamCommander);

		// exit if no targets where found
		if (targetList.isEmpty())
			return false;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get target
		PlayerEntity target = targetList.get(FIRST_INDEX);

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target is dead
		if (!target.isAlive())
			return false;
		
		// update target
		entity.setAttackTarget(target);
		
		return true;
	}

	@Override
	public void tick(LivingEntity commander, CreatureEntity entity) {
		// NO-OP
	}

}
