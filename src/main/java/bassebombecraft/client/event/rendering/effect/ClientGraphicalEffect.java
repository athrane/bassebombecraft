package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;

/**
 * CLIENT side implementation of {@linkplain GraphicalEffect} interface. Used by
 * {@linkplain ClientGraphicalEffectRepository} to store effects.
 */
public class ClientGraphicalEffect implements GraphicalEffect {

	/**
	 * Source entity.
	 */
	final Entity source;

	/**
	 * Target entity.
	 */
	final Entity target;

	/**
	 * ID used for registration and lookup of duration.
	 */
	String id;

	/**
	 * Effect operator.
	 */
	Operator2 effectOp;

	/**
	 * Constructor.
	 * 
	 * @param source           source entity.
	 * @param target           target entity.
	 * @param duration         duration of effect in measured in ticks.
	 * @param effectOp         effect operator.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	ClientGraphicalEffect(Entity source, Entity target, int duration, Operator2 effectOp,
			Consumer<String> cRemovalCallback) {
		this.source = source;
		this.target = target;
		this.effectOp = effectOp;
		id = Integer.toString(source.getEntityId());

		// register effect with client duration repository
		DurationRepository repository = getProxy().getClientDurationRepository();
		repository.add(id, duration, cRemovalCallback);
	}

	@Override
	public Entity getSource() {
		return source;
	}

	@Override
	public Entity getTarget() {
		return target;
	}

	public int getDuration() {
		return getCharmDuration(id, getProxy().getClientDurationRepository());
	}
	
	@Override
	public Operator2 getEffectOperator() {
		return effectOp;
	}

	/**
	 * Factory method.
	 * 
	 * @param source           source entity.
	 * @param target           target entity.
	 * @param duration         duration of effect in measured in ticks.
	 * @param effectOp         effect operator.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	public static GraphicalEffect getInstance(Entity source, Entity target, int duration, Operator2 effectOp,
			Consumer<String> cRemovalCallback) {
		return new ClientGraphicalEffect(source, target, duration, effectOp, cRemovalCallback);
	}

}
