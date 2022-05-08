package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.spawnCreeperArmyEntities;
import static bassebombecraft.config.ModConfiguration.spawnCreeperArmySpawnArea;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildCreeperArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

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
		spawnArea = spawnCreeperArmySpawnArea.get();
	}

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {

		try {

			// get shooter
			Entity shooter = projectile.getOwner();

			for (int i = 0; i < entities; i++) {

				// create creeper
				Creeper entity = EntityType.CREEPER.create(world);

				// calculate random spawn position
				setRandomSpawnPosition(projectile.blockPosition(), projectile.getYRot(), spawnArea, entity);

				// if shooter is a living entity then add entity to shooters team
				if (isTypeLivingEntity(shooter)) {
					getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
				}

				// if shooter is a living entity then configure AI
				if (isTypeLivingEntity(shooter)) {
					clearAllAiGoals(entity);
					buildCreeperArmyAi(entity, (LivingEntity) shooter);					
				}

				// spawn
				world.addFreshEntity(entity);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
