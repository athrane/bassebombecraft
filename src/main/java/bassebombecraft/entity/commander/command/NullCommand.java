package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;

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
	public boolean shouldExecute(LivingEntity commander, CreatureEntity entity) {
		return true;
	}

	@Override
	public void tick(LivingEntity commander, CreatureEntity entity) {
		// NO-OP 
	}

}
