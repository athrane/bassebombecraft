package bassebombecraft.operator.entity;

import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator} interface which spawn a
 * {@linkplain RavagerEntity}.
 * 
 * The entity is spawned at the invoker (e.g. the player).
 */
public class SpawnRavager implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnRavager.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_RAVAGER_ROAR;

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * SpawnRavager constructor.
	 * 
	 * @param splEntity invoker entity supplier.
	 */
	public SpawnRavager(Supplier<LivingEntity> splEntity) {
		this.splEntity = splEntity;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// get world
		World world = livingEntity.world;

		// create entity
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		RavagerEntity entity = EntityType.RAVAGER.create(world);
		entity.copyLocationAndAnglesFrom(livingEntity);

		// set AI
		clearAllAiGoals(entity);
		buildCharmedMobAi(entity, livingEntity);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}
}
