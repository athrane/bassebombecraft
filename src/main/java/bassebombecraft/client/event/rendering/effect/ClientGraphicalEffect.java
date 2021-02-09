package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.LivingEntity;

/**
 * CLIENT side implementation of {@linkplain GraphicalEffect} interface. Used by
 * {@linkplain ClientGraphicalEffectRepository} to store effects.
 */
public class ClientGraphicalEffect implements GraphicalEffect {

	/**
	 * Source entity.
	 */
	final LivingEntity source;

	/**
	 * Target entity.
	 */
	final LivingEntity target;

	/**
	 * ID used for registration and lookup of duration.
	 */
	String id;

	/**
	 * Constructor.
	 * 
	 * @param source           source entity.
	 * @param target           target entity.
	 * @param duration         duration of effect in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	ClientGraphicalEffect(LivingEntity source, LivingEntity target, int duration, Consumer<String> cRemovalCallback) {
		this.source = source;
		this.target = target;
		id = Integer.toString(source.getEntityId());

		// register effect with client duration repository
		DurationRepository repository = getProxy().getClientDurationRepository();
		repository.add(id, duration, cRemovalCallback);
	}

	@Override
	public LivingEntity getSource() {
		return source;
	}

	@Override
	public LivingEntity getTarget() {
		return target;
	}

	public int getDuration() {
		return getCharmDuration(id, getProxy().getClientDurationRepository());
	}

	/**
	 * Factory method.
	 * 
	 * @param source           source entity.
	 * @param target           target entity.
	 * @param duration         duration of effect in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	public static GraphicalEffect getInstance(LivingEntity source, LivingEntity target, int duration,
			Consumer<String> cRemovalCallback) {
		return new ClientGraphicalEffect(source, target, duration, cRemovalCallback);
	}

}
