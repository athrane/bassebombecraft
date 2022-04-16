package bassebombecraft.entity.commander.command;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

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
	public boolean shouldExecute(LivingEntity commander, PathfinderMob entity) {
		return true;
	}

	@Override
	public void tick(LivingEntity commander, PathfinderMob entity) {
		// NO-OP 
	}

}
