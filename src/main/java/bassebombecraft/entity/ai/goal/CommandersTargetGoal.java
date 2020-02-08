package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.hasTarget;
import static bassebombecraft.entity.EntityUtils.selfDestruct;
import static net.minecraft.entity.ai.goal.Goal.Flag.TARGET;

import java.util.EnumSet;
import java.util.Optional;

import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

/**
 * AI target acquisition goal which attacks commanders target.
 * 
 * Entity self-destructs if commander has died and aggros everything.
 */
public class CommandersTargetGoal extends Goal {

	/**
	 * Null/No target value to use when clearing the target.
	 */
	static final LivingEntity NO_TARGET = null;

	/**
	 * Goal owner.
	 */
	final CreatureEntity entity;

	/**
	 * Mob commander.
	 */
	LivingEntity commander;

	/**
	 * AiCommandersTargeting constructor.
	 * 
	 * @param entity    commanded entity.
	 * @param commander entity which commands entity.
	 */
	public CommandersTargetGoal(CreatureEntity entity, LivingEntity commander) {
		this.entity = entity;
		this.commander = commander;

		// "target" AI
		setMutexFlags(EnumSet.of(TARGET));
	}

	@Override
	public boolean shouldExecute() {

		// exit if commander is dead
		if (!commander.isAlive()) {
			selfDestruct(entity);
			return false;
		}

		// stop goal execution if no target is defined for commander
		if (!hasTarget(commander))
			return false;

		// get target
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		Optional<LivingEntity> optTarget = repository.getFirst(commander);

		// exit if target isn't defined (anymore)
		if (!optTarget.isPresent())
			return false;

		// continue goal execution if target is alive
		return (optTarget.get().isAlive());
	}

	@Override
	public void tick() {

		// get target
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		Optional<LivingEntity> optTarget = repository.getFirst(commander);

		// exit if target isn't defined (anymore)
		if (!optTarget.isPresent())
			return;

		// update target
		entity.setAttackTarget(optTarget.get());
	}

	@Override
	public void resetTask() {
		// clear target
		entity.setAttackTarget(NO_TARGET);
	}

}
