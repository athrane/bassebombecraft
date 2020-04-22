package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.assignAiGoals;
import static bassebombecraft.entity.ai.AiUtils.assignAiTargetGoals;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

/**
 * Default implementation of the {@linkplain CharmedMobsRepository}.
 */
public class DefaultCharmedMobsRepository implements CharmedMobsRepository {

	/**
	 * Charm duration.
	 */
	@Deprecated
	static final int EFFECT_DURATION = 1000; // Measured in ticks

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires a
	 * {@linkplain CharmedMob} added by this repository.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the expired element will
	 * be removed from this repository as well.
	 */
	Consumer<String> cRemovalCallback = id -> remove(id);

	/**
	 * Charmed mobs.
	 */
	Map<String, CharmedMob> charmedMobs = new ConcurrentHashMap<String, CharmedMob>();

	@Override
	public void add(MobEntity entity, LivingEntity commander) {

		// exit if entity is team member
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		if (teamRepository.isMember(commander, entity))
			return;

		// create charmed mob container
		CharmedMob charmedMob = CharmedMob.getInstance(entity, EFFECT_DURATION, cRemovalCallback);

		clearAllAiGoals(entity);
		buildCharmedMobAi(entity, commander);

		// store mob
		String id = Integer.toString(entity.getEntityId());
		charmedMobs.put(id, charmedMob);
	}

	@Override
	public void remove(String id) {
		if (!contains(id))
			return;

		// get entity
		CharmedMob charmedMob = charmedMobs.get(id);
		clearAllAiGoals(charmedMob.getEntity());
		assignAiGoals(charmedMob.getEntity(), charmedMob.getGoals());
		assignAiTargetGoals(charmedMob.getEntity(), charmedMob.getTargetGoals());

		// remove mob from repository
		charmedMobs.remove(id);

	}

	@Override
	public boolean contains(String id) {
		return charmedMobs.containsKey(id);
	}
		
	@Override
	public boolean contains(MobEntity entity) {
		int id = entity.getEntityId();
		String idAsString = Integer.toString(id);
		return charmedMobs.containsKey(idAsString);
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
