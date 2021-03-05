package bassebombecraft.client.event.charm;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.charmDuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

/**
 * CLIENT side implementation of the {@linkplain CharmedMobsRepository}.
 */
public class ClientCharmedMobsRepository implements CharmedMobsRepository {

	/**
	 * Repository identifier (for configuration).
	 */
	public static final String NAME = ClientCharmedMobsRepository.class.getSimpleName();

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

		// create charmed mob container
		int duration = charmDuration.get();
		CharmedMob charmedMob = ClientCharmedMob.getInstance(entity, duration);

		// exit if mob is already charmed
		if(contains(charmedMob.getId())) return;
		
		// register charmed mob with client duration repository
		DurationRepository repository = getProxy().getClientDurationRepository();
		repository.add(charmedMob.getId(), duration, cRemovalCallback);
		
		// store mob
		charmedMobs.put(charmedMob.getId(), charmedMob);
	}

	@Override
	public void remove(String id) {
		if (!contains(id))
			return;

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
		return new ClientCharmedMobsRepository();
	}

}
