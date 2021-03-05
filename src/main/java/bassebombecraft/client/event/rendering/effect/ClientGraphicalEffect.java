package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

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
	 * @param source   source entity.
	 * @param target   target entity.
	 * @param duration duration of effect in measured in ticks.
	 * @param effectOp effect operator.
	 */
	ClientGraphicalEffect(Entity source, Entity target, int duration, Operator2 effectOp) {
		this.source = source;
		this.target = target;
		this.effectOp = effectOp;
		id = Integer.toString(hashCode());
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
	
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Factory method.
	 * 
	 * @param source   source entity.
	 * @param target   target entity.
	 * @param duration duration of effect in measured in ticks.
	 * @param effectOp effect operator.
	 */
	public static GraphicalEffect getInstance(Entity source, Entity target, int duration, Operator2 effectOp) {
		return new ClientGraphicalEffect(source, target, duration, effectOp);
	}

}
