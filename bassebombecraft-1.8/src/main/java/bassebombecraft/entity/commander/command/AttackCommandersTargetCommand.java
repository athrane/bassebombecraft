package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.EntityPlayer;

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
	public boolean shouldExecute(EntityPlayer commander, EntityCreature entity) {

		// get target
		LivingEntity target = commander.getLastAttackedEntity();
		// start execution if target is defined
		return (target != null);
	}

	@Override
	public boolean continueExecuting(EntityPlayer commander, EntityCreature entity) {

		// get target
		LivingEntity target = commander.getLastAttackedEntity();

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target is dead
		if (target.isDead)
			return false;

		// update target
		entity.setAttackTarget(target);

		return true;
	}

}
