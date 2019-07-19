package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildKittenArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import java.util.List;
import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns an army of
 * kittens.
 */
public class SpawnKittenArmy implements ProjectileAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = SpawnKittenArmy.class.getSimpleName();

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
	 * Generate and render custom kitten name.
	 */
	final boolean renderCustomName;

	/**
	 * Custom kitten names.
	 */
	final List<String> names;

	/**
	 * SpawnKittenArmy constructor
	 */
	public SpawnKittenArmy() {
		Config configuration = getBassebombeCraft().getConfiguration();
		age = configuration.getInt(CONFIG_KEY + ".Age");
		kittens = configuration.getInt(CONFIG_KEY + ".Kittens");
		spawnSize = configuration.getInt(CONFIG_KEY + ".SpawnSize");
		renderCustomName = configuration.getBoolean(CONFIG_KEY + ".RenderCustomName");
		names = configuration.getStringList(CONFIG_KEY + ".Names");
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < kittens; i++) {

			// create ocelot
			OcelotEntity entity = new OcelotEntity(world);

			// set age
			if (i == 0)
				entity.setGrowingAge(1);
			else
				entity.setGrowingAge(age);

			// set tamed
			entity.setTamed(true);
			entity.setTameSkin(CAT_TYPE);

			
			// set owner
			LivingEntity owner = projectile.getThrower();
			entity.setOwnerId(owner.getUniqueID());

			// set in love with player
			if (owner instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) owner;
				entity.setInLove(player);
			}

			// calculate random position
			Random random = entity.getRNG();
			int randomX = random.nextInt(spawnSize) - (spawnSize / 2);
			int randomZ = random.nextInt(spawnSize) - (spawnSize / 2);
			double positionX = projectile.posX + randomX;
			double positionY = projectile.posY;
			double positionZ = projectile.posZ + randomZ;
			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);

			// add entity to team
			TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
			teamRepository.add(owner, entity);
			
			// set AI
			clearAiTasks(entity);
			buildKittenArmyAi(entity, owner);

			// set name
			if (renderCustomName) {
				entity.setCustomNameTag(getKittenName(random, i));
				entity.setAlwaysRenderNameTag(true);
			}

			// spawn
			world.spawnEntity(entity);
		}
	}

	/**
	 * Return random kitten name from list.
	 * 
	 * @param random
	 *            random object.
	 * @param index
	 *            kitten index.
	 * @return random kitten name from list. If kitten index is 0 then "Cloud"
	 *         is returned".
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
