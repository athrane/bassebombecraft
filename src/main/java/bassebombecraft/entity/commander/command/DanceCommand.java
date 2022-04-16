package bassebombecraft.entity.commander.command;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository.Commands;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

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
	public boolean shouldExecute(LivingEntity commander, PathfinderMob entity) {
		return true;
	}

	@Override
	public void tick(LivingEntity commander, PathfinderMob entity) {

		// clear target
		entity.setTarget(null);

		// set jumping
		if (shouldJump(entity)) {
			entity.getJumpControl().jump();

			Random random = getBassebombeCraft().getRandom();
			float strafe = random.nextInt(10) - 5;
			float vertical = 0;
			float forward = random.nextInt(10) - 5;
			Vec3 danceVec = new Vec3(strafe, vertical, forward);
			entity.travel(danceVec);
		}

	}

	/**
	 * Return true if entity should jump.
	 * 
	 * @param entity entity which might jump.
	 * @return true if entity should jump.
	 */
	boolean shouldJump(PathfinderMob entity) {
		Random random = getBassebombeCraft().getRandom();
		return random.nextDouble() > jumpChance;
	}

}
