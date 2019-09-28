package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.assignAiGoals;
import static bassebombecraft.entity.ai.AiUtils.assignAiTargetGoals;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

/**
 * Default implementation of the {@linkplain CharmedMobsRepository}.
 */
public class DefaultCharmedMobsRepository implements CharmedMobsRepository {

	static final int EFFECT_DURATION = 1000; // Measured in ticks

	/**
	 * Charmed mobs.
	 */
	Map<MobEntity, CharmedMob> charmedMobs = new ConcurrentHashMap<MobEntity, CharmedMob>();

	@Override
	public void add(MobEntity entity, LivingEntity commander) {

		// exist if entity is team member
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		if (teamRepository.isMember(commander, entity))
			return;

		// create charmed mob container
		CharmedMob charmedMob = CharmedMob.getInstance(entity, EFFECT_DURATION);

		clearAllAiGoals(entity);
		buildCharmedMobAi(entity, commander);

		// store mob
		charmedMobs.put(entity, charmedMob);
	}

	@Override
	public void remove(MobEntity entity) {
		if (!contains(entity))
			return;

		// restore AI tasks
		CharmedMob charmedMob = charmedMobs.get(entity);
		clearAllAiGoals(entity);
		assignAiGoals(entity, charmedMob.getGoals());
		assignAiTargetGoals(entity, charmedMob.getTargetGoals());

		// remove mob from repository
		charmedMobs.remove(entity);
	}

	@Override
	public void update(MobEntity entity) {
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
	public boolean contains(MobEntity entity) {
		return charmedMobs.containsKey(entity);
	}

	@Override
	public Collection<CharmedMob> get() {
		return charmedMobs.values();
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
