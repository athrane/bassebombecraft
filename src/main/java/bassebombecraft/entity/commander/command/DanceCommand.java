package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Dance command.
 */
public class DanceCommand implements MobCommand {

	/**
	 * Action identifier.
	 */
	public static final String NAME = DanceCommand.class.getSimpleName();

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
		jumpChance = ModConfiguration.danceCommandChance.get();
	}

	@Override
	public boolean shouldExecute(LivingEntity commander, CreatureEntity entity) {
		return true;
	}

	@Override
	public void tick(LivingEntity commander, CreatureEntity entity) {

		// clear target
		entity.setAttackTarget(null);

		// set jumping
		if (shouldJump(entity)) {
			entity.getJumpController().setJumping();

			Random random = getBassebombeCraft().getRandom();
			float strafe = random.nextInt(10) - 5;
			float vertical = 0;
			float forward = random.nextInt(10) - 5;
			Vector3d danceVec = new Vector3d(strafe, vertical, forward);
			entity.travel(danceVec);
		}

	}

	/**
	 * Return true if entity should jump.
	 * 
	 * @param entity entity which might jump.
	 * @return true if entity should jump.
	 */
	boolean shouldJump(CreatureEntity entity) {
		Random random = getBassebombeCraft().getRandom();
		return random.nextDouble() > jumpChance;
	}

}
