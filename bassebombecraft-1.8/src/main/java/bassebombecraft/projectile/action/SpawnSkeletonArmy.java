package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildSkeletonArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import java.util.Random;

import com.typesafe.config.Config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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

	static final int CAT_TYPE = 3;
	static final float PITCH = 0.0F;

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
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < skeletons; i++) {

			// create skeleton
			EntitySkeleton entity = new EntitySkeleton(world);
			entity.setSwingingArms(true);

			// calculate random position
			Random random = entity.getRNG();
			int randomX = random.nextInt(spawnSize) - (spawnSize / 2);
			int randomZ = random.nextInt(spawnSize) - (spawnSize / 2);
			double positionX = projectile.posX + randomX;
			double positionY = projectile.posY;
			double positionZ = projectile.posZ + randomZ;
			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);

			// add bow
			ItemStack bowStack = new ItemStack(Items.BOW);
			entity.setHeldItem(EnumHand.MAIN_HAND, bowStack);
			
			// helmet
			ItemStack helmetstack = new ItemStack(Items.DIAMOND_HELMET);
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmetstack );
			
			// get owner
			EntityLivingBase thrower = projectile.getThrower();
						
			// set AI
			clearAiTasks(entity);
			buildSkeletonArmyAi(entity, thrower);

			// spawn
			world.spawnEntity(entity);
		}
	}

}
