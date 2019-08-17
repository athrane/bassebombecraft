package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildSkeletonArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a skeleton
 * army.
 */
public class SpawnSkeletonArmy implements ProjectileAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = SpawnSkeletonArmy.class.getSimpleName();

	/**
	 * Skeleton pitch.
	 */
	final static float PITCH = 0.0F;

	/**
	 * Skeletons.
	 */
	final int skeletons;

	/**
	 * Spawn size in blocks.
	 */
	final int spawnSize;

	/**
	 * SpawnSkeletonArmy constructor
	 */
	public SpawnSkeletonArmy() {
		Config configuration = getBassebombeCraft().getConfiguration();
		skeletons = configuration.getInt(CONFIG_KEY + ".Skeletons");
		spawnSize = configuration.getInt(CONFIG_KEY + ".SpawnSize");
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < skeletons; i++) {

			// create skeleton
			SkeletonEntity entity = EntityType.SKELETON.create(world);

			// calculate random position
			Random random = entity.getRNG();
			int randomX = random.nextInt(spawnSize) - (spawnSize / 2);
			int randomZ = random.nextInt(spawnSize) - (spawnSize / 2);
			double positionX = projectile.posX + randomX;
			double positionY = projectile.posY;
			double positionZ = projectile.posZ + randomZ;
			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);

			// add bow
			entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));

			// helmet
			ItemStack helmetstack = new ItemStack(Items.DIAMOND_HELMET);
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, helmetstack);

			// get owner
			LivingEntity thrower = projectile.getThrower();

			// add entity to team
			TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
			teamRepository.add(thrower, entity);

			// set AI
			clearAllAiGoals(entity);
			buildSkeletonArmyAi(entity, thrower);

			// spawn
			world.addEntity(entity);
		}
	}

}
