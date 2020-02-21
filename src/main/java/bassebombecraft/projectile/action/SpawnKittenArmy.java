package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.ai.AiUtils.buildKittenArmyAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.List;
import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.entity.team.TeamRepository;
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
		age = ModConfiguration.spawnKittenArmyAge.get();
		kittens = ModConfiguration.spawnKittenArmyEntities.get();
		spawnSize = ModConfiguration.spawnKittenArmySpawnArea.get();
		names = ModConfiguration.spawnKittenArmyNames.get();
	}

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		for (int i = 0; i < kittens; i++) {

			// create cat
			CatEntity entity = EntityType.CAT.create(world);

			// set age
			if (i == 0)
				entity.setGrowingAge(1);
			else
				entity.setGrowingAge(age);

			// set owner
			LivingEntity owner = projectile.getThrower();

			// set tamed by player
			if (isTypePlayerEntity(owner)) {
				PlayerEntity player = (PlayerEntity) owner;
				entity.setTamedBy(player);
			}

			// calculate random spawn position
			setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, spawnSize, entity);

			// add entity to team
			TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
			teamRepository.add(owner, entity);

			// set AI
			clearAllAiGoals(entity);
			buildKittenArmyAi(entity, owner);

			// set name
			Random random = getBassebombeCraft().getRandom();
			ITextComponent name = new StringTextComponent(getKittenName(random, i));
			entity.setCustomName(name);
			entity.setCustomNameVisible(true);

			// spawn
			world.addEntity(entity);
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
