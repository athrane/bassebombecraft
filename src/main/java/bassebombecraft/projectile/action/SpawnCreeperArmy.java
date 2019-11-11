package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildCreeperArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import bassebombecraft.config.ModConfiguration;
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
	 * Action identifier.
	 */
	public final static String NAME = SpawnCreeperArmy.class.getSimpleName();
	
	/**
	 * Skeleton pitch.
	 */
	final static float PITCH = 0.0F;

	/**
	 * Number of entities.
	 */
	final int entities;

	/**
	 * Spawn area in blocks.
	 */
	final int spawnArea;

	/**
	 * SpawnCreeperArmy constructor.
	 */
	public SpawnCreeperArmy() {
		entities = ModConfiguration.spawnCreeperArmyEntities.get();
		spawnArea = ModConfiguration.spawnCreeperArmySpawnArea.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < entities; i++) {

			// create creeper
			CreeperEntity entity = EntityType.CREEPER.create(world);

			// calculate random spawn position
			setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, spawnArea, entity);

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
