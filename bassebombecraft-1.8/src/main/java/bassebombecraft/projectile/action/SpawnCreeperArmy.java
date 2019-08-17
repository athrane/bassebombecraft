package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildCreeperArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a creeper
 * army.
 */
public class SpawnCreeperArmy implements ProjectileAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = SpawnCreeperArmy.class.getSimpleName();

	/**
	 * Skeleton pitch.
	 */
	final static float PITCH = 0.0F;

	/**
	 * Creepers.
	 */
	final int creepers;

	/**
	 * Spawn size in blocks.
	 */
	final int spawnSize;

	/**
	 * SpawnSkeletonArmy constructor
	 */
	public SpawnCreeperArmy() {
		Config configuration = getBassebombeCraft().getConfiguration();
		creepers = configuration.getInt(CONFIG_KEY + ".Creepers");
		spawnSize = configuration.getInt(CONFIG_KEY + ".SpawnSize");
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < creepers; i++) {

			// create skeleton
			CreeperEntity entity = EntityType.CREEPER.create(world);

			// calculate random position
			Random random = entity.getRNG();
			int randomX = random.nextInt(spawnSize) - (spawnSize / 2);
			int randomZ = random.nextInt(spawnSize) - (spawnSize / 2);
			double positionX = projectile.posX + randomX;
			double positionY = projectile.posY;
			double positionZ = projectile.posZ + randomZ;
			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);

			// get owner
			LivingEntity thrower = projectile.getThrower();

			// add entity to team
			TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
			teamRepository.add(thrower, entity);

			// set AI
			clearAllAiGoals(entity);
			buildCreeperArmyAi(entity, thrower);

			// spawn
			world.addEntity(entity);
		}
	}

}
