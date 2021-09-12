package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * CLIENT side implementation of {@linkplain GraphicalEffect} interface.
 * 
 * Used by {@linkplain ClientGraphicalEffectRepository} to store effects.
 * 
 * Used by {@linkplain EffectRenderer} to render effects.
 * 
 * Support persistence of vectors #1 in ports between operator invocations for
 * rendering.
 */
public class ClientGraphicalEffect implements GraphicalEffect {

	/**
	 * Source Id if entity unresolved.
	 */
	final int sourceId;

	/**
	 * Source entity.
	 */
	Entity source;

	/**
	 * Target entity.
	 */
	final Entity target;

	/**
	 * ID used for registration and lookup of duration.
	 */
	final String id;

	/**
	 * Duration.
	 */
	final int duration;

	/**
	 * Effect operator.
	 */
	final Operator2 effectOp;

	/**
	 * Vectors used for rendering effects.
	 */
	Vector3d[] vectors = new Vector3d[0];

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
		this.sourceId = Integer.MIN_VALUE;
	}

	/**
	 * Constructor for unresolved instance.
	 * 
	 * @param sourceId source entity ID.
	 * @param target   target entity.
	 * @param duration duration of effect in measured in ticks.
	 * @param effectOp effect operator.
	 */
	ClientGraphicalEffect(int sourceId, Entity target, int duration, Operator2 effectOp) {
		this.sourceId = sourceId;
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

		// render if resolved
		if (source != null) {
			doRender(ports);
			return;
		}

		// attempt to resolve, get client side entity from ID
		Minecraft mcClient = Minecraft.getInstance();
		source = mcClient.world.getEntityByID(sourceId);
	}

	/**
	 * Do rendering
	 * 
	 * @param ports ports object used for rendering.
	 */
	void doRender(Ports ports) {
		ports.setEntity1(source);
		ports.setEntity2(target);

		// get remaining duration of effect
		DurationRepository repository = getProxy().getClientDurationRepository();
		int remainingDuration = repository.get(id);

		// store overwrite integer #1 with remaining duration
		ports.setInteger1(remainingDuration);

		// add stored vectors
		ports.setVectors1(vectors);
		run(ports, effectOp);

		// capture vectors
		vectors = ports.getVectors1();
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

	/**
	 * Factory method for unresolved instance.
	 * 
	 * @param sourceId source entity ID.
	 * @param target   target entity.
	 * @param duration duration of effect in measured in ticks.
	 * @param effectOp effect operator.
	 */
	public static GraphicalEffect getInstance(int sourceId, Entity target, int duration, Operator2 effectOp) {
		return new ClientGraphicalEffect(sourceId, target, duration, effectOp);
	}

}
