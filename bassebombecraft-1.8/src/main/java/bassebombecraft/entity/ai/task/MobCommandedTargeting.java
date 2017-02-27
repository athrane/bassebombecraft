package bassebombecraft.entity.ai.task;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

/**
 * AI target acquisition task which is command the by entitys mob commander.
 */
public class MobCommandedTargeting extends EntityAITarget {

	/**
	 * Null target.
	 */
	static final EntityLivingBase NULL_TARGET = null;

	/**
	 * Mob commander.
	 */
	EntityPlayer commander;

	/**
	 * MobCommandedTarget constructor.
	 * 
	 * @param owner
	 *            commanded entity.
	 * @param commander
	 *            entity which commands entity.
	 */
	public MobCommandedTargeting(EntityCreature owner, EntityPlayer commander) {
		super(owner, false);
		this.commander = commander;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {

		// register player and get command
		MobCommanderRepository repository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = repository.getCommand(commander);

		// initialize command
		return command.shouldExecute(commander, this.taskOwner);
	}

	@Override
	public boolean continueExecuting() {

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
