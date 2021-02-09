package bassebombecraft.client.event.rendering.effect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.LivingEntity;

/**
 * CLIENT side implementation of the {@linkplain GraphicalEffectRepository}
 * interface.
 */
public class ClientGraphicalEffectRepository implements GraphicalEffectRepository {

	/**
	 * Repository identifier (for configuration).
	 */
	public static final String NAME = ClientGraphicalEffectRepository.class.getSimpleName();

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires a
	 * {@linkplain GraphicalEffect} added by this repository.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the expired element will
	 * be removed from this repository as well.
	 */
	Consumer<String> cRemovalCallback = id -> remove(id);

	/**
	 * Effects.
	 */
	Map<String, GraphicalEffect> effects = new ConcurrentHashMap<String, GraphicalEffect>();

	@Override
	public void add(LivingEntity source, LivingEntity target, int duration) {
		// create effect container
		GraphicalEffect effect = ClientGraphicalEffect.getInstance(source, target, duration, cRemovalCallback);

		// store effect
		String id = Integer.toString(source.getEntityId());
		effects.put(id, effect);
	}

	@Override
	public void remove(String id) {
		if (!contains(id))
			return;

		// remove effect from repository
		effects.remove(id);
	}

	@Override
	public boolean contains(String id) {
		return effects.containsKey(id);
	}

	@Override
	public Stream<GraphicalEffect> get() {
		return effects.values().stream();
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static GraphicalEffectRepository getInstance() {
		return new ClientGraphicalEffectRepository();
	}

}
