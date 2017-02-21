package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Null mob command.
 */
public class Wait implements MobCommand {

	@Override
	public Commands getType() {
		return Commands.WAIT;
	}

	@Override
	public String getTitle() {
		return "Wait";
	}

	@Override
	public boolean shouldExecute(EntityPlayer commander) {
		return true;
	}

	@Override
	public boolean continueExecuting(EntityPlayer commander, EntityCreature entity) {

		// set jumping
		entity.setJumping(true);
		entity.getJumpHelper().setJumping();
		entity.getJumpHelper().doJump();

		return true;
	}

}
