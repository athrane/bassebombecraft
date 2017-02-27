package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Null mob command.
 */
public class NullCommand implements MobCommand {

	@Override
	public Commands getType() {
		return Commands.NULL;
	}

	@Override
	public String getTitle() {
		return "No order";
	}

	@Override
	public boolean shouldExecute(EntityPlayer commander, EntityCreature entity) {
		return true;
	}

	@Override
	public boolean continueExecuting(EntityPlayer commander, EntityCreature entity) {
		return true;
	}

}
