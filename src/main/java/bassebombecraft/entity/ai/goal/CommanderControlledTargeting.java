package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.TARGET;

import java.util.EnumSet;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * AI target acquisition goal which is commanded the by mob commander.
 */
public class CommanderControlledTargeting extends Goal {

	/**
	 * Null target.
	 */
	static final LivingEntity NULL_TARGET = null;

	/**
	 * Goal owner.
	 */
	final PathfinderMob entity;

	/**
	 * Mob commander.
	 */
	LivingEntity commander;

	/**
	 * CommanderControlledTargeting constructor.
	 * 
	 * @param entity    commanded entity.
	 * @param commander entity which commands entity.
	 */
	public CommanderControlledTargeting(PathfinderMob entity, LivingEntity commander) {
		this.entity = entity;
		this.commander = commander;

		// "target" AI
		setFlags(EnumSet.of(TARGET));
	}

	@Override
	public boolean canUse() {
		try {
			// register player and get command
			MobCommanderRepository repository = getProxy().getServerMobCommanderRepository();
			MobCommand command = repository.getCommand(commander);

			// initialize command
			return command.shouldExecute(commander, entity);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// don't execute as we have a error
			return false;
		}
	}

	@Override
	public void tick() {
		try {

			// register player and get command
			MobCommanderRepository repository = getProxy().getServerMobCommanderRepository();
			MobCommand command = repository.getCommand(commander);

			// execute
			command.tick(commander, entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
