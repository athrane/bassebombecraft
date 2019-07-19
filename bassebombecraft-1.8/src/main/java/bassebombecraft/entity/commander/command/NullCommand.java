package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.player.PlayerEntity;

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
	public boolean shouldExecute(PlayerEntity commander, CreatureEntity entity) {
		return true;
	}

	@Override
	public boolean continueExecuting(PlayerEntity commander, CreatureEntity entity) {
		return true;
	}

}
