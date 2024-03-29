package bassebombecraft.entity.commander;

import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

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
	 * @param commander commander to initialize command from.
	 * @param entity    entity commanded by mob commander.
	 * 
	 * @return true if command should start to execute.
	 */
	public boolean shouldExecute(LivingEntity commander, PathfinderMob entity);

	/**
	 * Execute command.
	 */
	public void tick(LivingEntity commander, PathfinderMob entity);
}
