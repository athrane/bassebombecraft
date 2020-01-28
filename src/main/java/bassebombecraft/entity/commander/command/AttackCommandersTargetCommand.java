package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Optional;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;

/**
 * Attack commander's target command.
 */
public class AttackCommandersTargetCommand implements MobCommand {

	@Override
	public Commands getType() {
		return Commands.COMMANDERS_TARGET;
	}

	@Override
	public String getTitle() {
		return "Attack commander's target";
	}

	@Override
	public boolean shouldExecute(LivingEntity commander, CreatureEntity entity) {

		// get target
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		Optional<LivingEntity> optTarget = repository.getFirst(commander);
		
		// exit if target is undefined
		if (!optTarget.isPresent())
			return false;

		// exit if target is dead
		if (!optTarget.get().isAlive())
			return false;

		// update target
		entity.setAttackTarget(optTarget.get());

		return true;
	}

	@Override
	public void tick(LivingEntity commander, CreatureEntity entity) {
		// NO-OP
	}

}
