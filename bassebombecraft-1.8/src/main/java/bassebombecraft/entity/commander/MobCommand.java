package bassebombecraft.entity.commander;

import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for mob command.
 */
public interface MobCommand {

	/**
	 * Get command type.
	 * 
	 * @return command type.
	 */
	Commands getType();

	/**
	 * Get command title.
	 * 
	 * @return command title.
	 */
	String getTitle();

	/**
	 * Initialize command if command should be executed.
	 * 
	 * @param commander
	 *            commander to initialize command from.
	 * @param entity
	 *            entity commanded by mob commander.
	 * 
	 * @return true if command should start to execute.
	 */
	boolean shouldExecute(PlayerEntity commander, EntityCreature entity);

	/**
	 * Execute command.
	 * 
	 * @param commander
	 *            commander.
	 * @param entity
	 *            entity commanded by mob commander.
	 * 
	 * @return true if command should continue to execute.
	 */
	boolean continueExecuting(PlayerEntity commander, EntityCreature entity);
}
