package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.spawnKittenArmyAge;
import static bassebombecraft.config.ModConfiguration.spawnKittenArmyEntities;
import static bassebombecraft.config.ModConfiguration.spawnKittenArmyNames;
import static bassebombecraft.config.ModConfiguration.spawnKittenArmySpawnArea;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildKittenArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns an army of
 * kittens.
 */
public class SpawnKittenArmy implements ProjectileAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = SpawnKittenArmy.class.getSimpleName();

	static final int CAT_TYPE = 3;
	static final float PITCH = 0.0F;

	/**
	 * Kitten age.
	 */
	final int age;

	/**
	 * Kitten age.
	 */
	final int kittens;

	/**
	 * Spawn size in blocks.
	 */
	final int spawnSize;

	/**
	 * Custom kitten names.
	 */
	final List<? extends String> names;

	/**
	 * SpawnKittenArmy constructor
	 */
	public SpawnKittenArmy() {
		age = spawnKittenArmyAge.get();
		kittens = spawnKittenArmyEntities.get();
		spawnSize = spawnKittenArmySpawnArea.get();
		names = spawnKittenArmyNames.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		try {
			// get shooter
			Entity shooter = projectile.getShooter();
			
			for (int i = 0; i < kittens; i++) {

				// create cat
				CatEntity entity = EntityType.CAT.create(world);

				// set age
				if (i == 0)
					entity.setGrowingAge(1);
				else
					entity.setGrowingAge(age);

				// set tamed by player
				if (isTypePlayerEntity(shooter)) {
					PlayerEntity player = (PlayerEntity) shooter;
					entity.setTamedBy(player);
				}

				// calculate random spawn position
				setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, spawnSize, entity);

				// if shooter is a living entity then add entity to shooters team
				if (isTypeLivingEntity(shooter)) {
					getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
				}

				// if shooter is a living entity then configure AI
				if (isTypeLivingEntity(shooter)) {
					clearAllAiGoals(entity);
					buildKittenArmyAi(entity, (LivingEntity) shooter);					
				}
				
				// set name
				Random random = getBassebombeCraft().getRandom();
				ITextComponent name = new StringTextComponent(getKittenName(random, i));
				entity.setCustomName(name);
				entity.setCustomNameVisible(true);

				// spawn
				world.addEntity(entity);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Return random kitten name from list.
	 * 
	 * @param random random object.
	 * @param index  kitten index.
	 * 
	 * @return random kitten name from list.
	 */
	String getKittenName(Random random, int index) {
		if (index == 0) {
			return names.get(index);
		}
		int size = names.size();
		int randomIndex = random.nextInt(size - 1);
		return names.get(randomIndex + 1);
	}

}
