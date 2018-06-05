package bassebombecraft.event.charm;

import static bassebombecraft.entity.ai.AiUtils.assignAiTargetTasks;
import static bassebombecraft.entity.ai.AiUtils.assignAiTasks;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * Default implementation of the {@linkplain CharmedMobsRepository}.
 */
public class DefaultCharmedMobsRepository implements CharmedMobsRepository {

	static final int EFFECT_DURATION = 1000; // Measured in ticks

	/**
	 * Charmed mobs.
	 */
	Map<EntityLiving, CharmedMob> charmedMobs = new ConcurrentHashMap<EntityLiving, CharmedMob>();

	@Override
	public void add(EntityLiving entity, EntityLivingBase commander) {

		// create charmed mob container
		CharmedMob charmedMob = new CharmedMob(entity, EFFECT_DURATION);

		clearAiTasks(entity);
		buildCharmedMobAi(entity, commander);

		// store mob
		charmedMobs.put(entity, charmedMob);
	}

	@Override
	public void remove(EntityLiving entity) {
		if (!contains(entity))
			return;

		// restore AI tasks
		CharmedMob charmedMob = charmedMobs.get(entity);
		clearAiTasks(entity);
		assignAiTasks(entity, charmedMob.getTasks());
		assignAiTargetTasks(entity, charmedMob.getTargetTasks());

		// remove mob from repository
		charmedMobs.remove(entity);
	}

	@Override
	public void update(EntityLiving entity) {
		if (!contains(entity))
			return;

		// update
		CharmedMob charmedMob = charmedMobs.get(entity);
		charmedMob.update();

		// remove if charm is expired
		if (charmedMob.isCharmExpired())
			remove(entity);
	}

	@Override
	public boolean contains(EntityLiving entity) {
		return charmedMobs.containsKey(entity);
	}
	
	@Override
	public Stream<CharmedMob> get() {
		return charmedMobs.entrySet().stream().map(e -> e.getValue());
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static CharmedMobsRepository getInstance() {
		return new DefaultCharmedMobsRepository();
	}

}
