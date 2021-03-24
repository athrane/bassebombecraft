package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.charmDuration;
import static bassebombecraft.entity.ai.AiUtils.assignAiGoals;
import static bassebombecraft.entity.ai.AiUtils.assignAiTargetGoals;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

/**
 * SERVER side implementation of the {@linkplain CharmedMobsRepository}.
 */
public class ServerCharmedMobsRepository implements CharmedMobsRepository {

	/**
	 * Repository identifier (for configuration).
	 */
	public static final String NAME = ServerCharmedMobsRepository.class.getSimpleName();

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
		try {

			// exit if entity is team member
			TeamRepository repository = getProxy().getServerTeamRepository();
			if (repository.isMember(commander, entity))
				return;
			
			// create charmed mob container
			Integer duration = charmDuration.get();
			CharmedMob charmedMob = ServerCharmedMob.getInstance(entity, duration);
			
			// exit if mob is already charmed
			if(contains(charmedMob.getId())) return;
			
			clearAllAiGoals(entity);
			buildCharmedMobAi(entity, commander);

			// store mob
			String id = Integer.toString(entity.getEntityId());
			charmedMobs.put(id, charmedMob);

			// register charmed mob with server duration repository
			DurationRepository durationRepository = getProxy().getServerDurationRepository();
			durationRepository.add(id, duration, cRemovalCallback);
			
			// send charm info to client
			getProxy().getNetworkChannel().sendAddCharmPacket(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
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
	public void remove(MobEntity entity) {
		int id = entity.getEntityId();
		String idAsString = Integer.toString(id);
		remove(idAsString);
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
	public Stream<CharmedMob> get() {
		return charmedMobs.values().stream();
	}

	@Override
	public int size() {
		return charmedMobs.size();
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static CharmedMobsRepository getInstance() {
		return new ServerCharmedMobsRepository();
	}

}
