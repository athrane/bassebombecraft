package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildSkeletonArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import static bassebombecraft.config.ModConfiguration.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

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
	public void execute(ThrowableProjectile projectile, Level world, HitResult result) {
		try {
			// get shooter
			Entity shooter = projectile.getOwner();
			
			for (int i = 0; i < entities; i++) {

				// create skeleton
				Skeleton entity = EntityType.SKELETON.create(world);

				// calculate random spawn position
				setRandomSpawnPosition(projectile.blockPosition(), projectile.getYRot(), spawnArea, entity);

				// add bow
				entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));

				// helmet
				ItemStack helmetstack = new ItemStack(Items.DIAMOND_HELMET);
				entity.setItemSlot(EquipmentSlot.HEAD, helmetstack);

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
				world.addFreshEntity(entity);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
