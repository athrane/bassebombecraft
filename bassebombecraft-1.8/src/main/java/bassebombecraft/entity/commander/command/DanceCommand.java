package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Dance command.
 */
public class DanceCommand implements MobCommand {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = DanceCommand.class.getSimpleName();

	/**
	 * Chance of jumping.
	 */
	final double jumpChance;

	@Override
	public Commands getType() {
		return Commands.STOP;
	}

	@Override
	public String getTitle() {
		return "Dance";
	}

	/**
	 * StopCommand constructor.
	 */
	public DanceCommand() {
		super();
		Config configuration = getBassebombeCraft().getConfiguration();
		jumpChance = configuration.getDouble(CONFIG_KEY + ".JumpChance");
	}

	@Override
	public boolean shouldExecute(PlayerEntity commander, EntityCreature entity) {
		return true;
	}

	@Override
	public boolean continueExecuting(PlayerEntity commander, EntityCreature entity) {

		// clear target
		entity.setAttackTarget(null);

		// set jumping
		if (shouldJump(entity)) {
			entity.getJumpHelper().setJumping();
			
			Random random = entity.getRNG();	
			float strafe = random.nextInt(10) - 5; 
			float vertical = 0;
			float forward = random.nextInt(10) - 5; 
			entity.travel(strafe, vertical, forward);
		}

		return true;
	}

	/**
	 * Return true if entity should jump.
	 * 
	 * @param entity
	 *            entity which might jump.
	 * @return true if entity should jump.
	 */
	boolean shouldJump(EntityCreature entity) {
		Random random = entity.getRNG();
		return random.nextDouble() > jumpChance;
	}

}
