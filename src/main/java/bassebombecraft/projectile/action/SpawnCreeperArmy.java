package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildCreeperArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import static bassebombecraft.config.ModConfiguration.*;
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
	public static final String NAME = SpawnCreeperArmy.class.getSimpleName();

	/**
	 * Skeleton pitch.
	 */
	static final float PITCH = 0.0F;

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
		entities = spawnCreeperArmyEntities.get();
		spawnArea =spawnCreeperArmySpawnArea.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {

		try {

			for (int i = 0; i < entities; i++) {

				// create creeper
				CreeperEntity entity = EntityType.CREEPER.create(world);

				// calculate random spawn position
				setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, spawnArea, entity);

				// get owner
				LivingEntity thrower = projectile.getThrower();

				// add entity to team
				getProxy().getTeamRepository(world).add(thrower, entity);

				// set AI
				clearAllAiGoals(entity);
				buildCreeperArmyAi(entity, thrower);

				// spawn
				world.addEntity(entity);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
