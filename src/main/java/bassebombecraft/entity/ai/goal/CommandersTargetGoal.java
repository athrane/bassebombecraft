package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.hasTarget;
import static bassebombecraft.entity.EntityUtils.selfDestruct;
import static net.minecraft.entity.ai.goal.Goal.Flag.TARGET;

importimport bassebombecraft.event.entity.target.TargetRepository;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

 javanet.minecraft.world.entity.ai.goal.Goal.Flagnal;

import bassebombecraft.event.entity.target.TargetRepository;
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
	final PathfinderMob entity;

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
	public CommandersTargetGoal(PathfinderMob entity, LivingEntity commander) {
		this.entity = entity;
		this.commander = commander;

		// "target" AI
		setFlags(EnumSet.of(TARGET));
	}

	@Override
	public boolean canUse() {
		try {
			// exit if commander is dead
			if (!commander.isAlive()) {
				selfDestruct(entity);
				return false;
			}

			// stop goal execution if no target is defined for commander
			if (!hasTarget(commander))
				return false;

			// get target
			TargetRepository repository = getProxy().getServerTargetRepository();
			Optional<LivingEntity> optTarget = repository.getFirst(commander);

			// exit if target isn't defined (anymore)
			if (!optTarget.isPresent())
				return false;

			// continue goal execution if target is alive
			return (optTarget.get().isAlive());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// don't execute since we had an error
			return false;
		}
	}

	@Override
	public void tick() {
		try {
			// get target
			TargetRepository repository = getProxy().getServerTargetRepository();
			Optional<LivingEntity> optTarget = repository.getFirst(commander);

			// exit if target isn't defined (anymore)
			if (!optTarget.isPresent())
				return;

			// update target
			entity.setTarget(optTarget.get());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void stop() {
		// clear target
		entity.setTarget(NO_TARGET);
	}

}
