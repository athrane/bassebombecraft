package bassebombecraft.client.event.rendering.effect;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;

/**
 * CLIENT side implementation of {@linkplain GraphicalEffect} interface.
 * 
 * Used by {@linkplain ClientGraphicalEffectRepository} to store effects.
 * 
 * Used by {@linkplain EffectRenderer} to render effects.
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
	 * Duration.
	 */
	int duration;

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
		this.id = Integer.toString(hashCode());
		this.duration = duration;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void render(Ports ports) {
		ports.setEntity1(source);
		ports.setEntity2(target);
		Operators2.run(ports, effectOp);
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
