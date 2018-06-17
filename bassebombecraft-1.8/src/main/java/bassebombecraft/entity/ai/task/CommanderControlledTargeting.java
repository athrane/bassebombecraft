package bassebombecraft.entity.ai.task;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

/**
 * AI target acquisition task which is commanded the by mob commander.
 */
public class CommanderControlledTargeting extends EntityAITarget {

	/**
	 * Null target.
	 */
	static final EntityLivingBase NULL_TARGET = null;

	/**
	 * Mob commander.
	 */
	EntityPlayer commander;

	/**
	 * CommanderControlledTargeting constructor.
	 * 
	 * @param owner
	 *            commanded entity.
	 * @param commander
	 *            entity which commands entity.
	 */
	public CommanderControlledTargeting(EntityCreature owner, EntityPlayer commander) {
		super(owner, false);
		this.commander = commander;

		// Uncategorised compatible with every task
		// this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		
		// register player and get command
		MobCommanderRepository repository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = repository.getCommand(commander);

		// initialize command
		return command.shouldExecute(commander, this.taskOwner);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */	
	@Override
	public boolean shouldContinueExecuting() {

		// register player and get command
		MobCommanderRepository repository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = repository.getCommand(commander);

		// execute command
		return command.continueExecuting(commander, this.taskOwner);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

}