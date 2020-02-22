package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildSkeletonArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import bassebombecraft.config.ModConfiguration;
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
		entities = ModConfiguration.spawnSkeletonArmyEntities.get();
		spawnArea = ModConfiguration.spawnSkeletonArmySpawnArea.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
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
