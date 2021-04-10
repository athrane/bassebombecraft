package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildSkeletonArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import static bassebombecraft.config.ModConfiguration.*;

import net.minecraft.entity.Entity;
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
	 * Action identifier.
	 */
	public static final String NAME = SpawnSkeletonArmy.class.getSimpleName();

	/**
	 * Skeleton pitch.
	 */
	static final float PITCH = 0.0F;

	/**
	 * Number of entities.
	 */
	final int entities;

	/**
	 * Spawn size in blocks.
	 */
	final int spawnArea;

	/**
	 * SpawnSkeletonArmy constructor
	 */
	public SpawnSkeletonArmy() {
		entities = spawnSkeletonArmyEntities.get();
		spawnArea = spawnSkeletonArmySpawnArea.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		try {
			// get shooter
			Entity shooter = projectile.getShooter();
			
			for (int i = 0; i < entities; i++) {

				// create skeleton
				SkeletonEntity entity = EntityType.SKELETON.create(world);

				// calculate random spawn position
				setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, spawnArea, entity);

				// add bow
				entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));

				// helmet
				ItemStack helmetstack = new ItemStack(Items.DIAMOND_HELMET);
				entity.setItemStackToSlot(EquipmentSlotType.HEAD, helmetstack);

				// if shooter is a living entity then add entity to shooters team
				if (isTypeLivingEntity(shooter)) {
					getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
				}

				// if shooter is a living entity then configure AI
				if (isTypeLivingEntity(shooter)) {
					clearAllAiGoals(entity);
					buildSkeletonArmyAi(entity, (LivingEntity) shooter);					
				}
				
				// spawn
				world.addEntity(entity);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
