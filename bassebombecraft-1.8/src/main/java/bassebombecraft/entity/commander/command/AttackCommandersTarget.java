package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.player.PlayerUtils.sendChatMessageToPlayer;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Null mob command.
 */
public class AttackCommandersTarget implements MobCommand {

	/**
	 * Commander target.
	 */
	EntityLivingBase target;

	@Override
	public Commands getType() {
		return Commands.COMMANDERS_TARGET;
	}

	@Override
	public String getTitle() {
		return "Attack commander's target";
	}

	@Override
	public boolean shouldExecute(EntityPlayer commander) {

		// get target
		target = commander.getLastAttacker();

		// start execution if target is defined
		return (target != null);
	}

	@Override
	public boolean continueExecuting(EntityPlayer commander, EntityCreature entity) {

		// get target
		target = commander.getLastAttacker();
		
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
