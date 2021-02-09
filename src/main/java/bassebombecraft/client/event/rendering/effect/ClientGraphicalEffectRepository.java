package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.ClientModConstants.LIGHTNING_LINE_COLOR;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bassebombecraft.client.op.rendering.InitElectrocute2;
import bassebombecraft.client.op.rendering.RenderLine2;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.operator.NullOp2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.Electrocute2;
import net.minecraft.entity.Entity;

/**
 * CLIENT side implementation of the {@linkplain GraphicalEffectRepository}
 * interface.
 */
public class ClientGraphicalEffectRepository implements GraphicalEffectRepository {

	/**
	 * Default effect operator.
	 */
	final static Operator2 DEFAULT_OPERATOR = new NullOp2();

	/**
	 * Effect operator for Electrocute effect.
	 */
	final static Operator2 ELECTROCUTE_OPERATOR = new Sequence2(new InitElectrocute2(),
			new RenderLine2(LIGHTNING_LINE_COLOR, LIGHTNING_LINES));

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
	public void add(Entity source, Entity target, int duration, String name) {

		// resolve effect operator
		Operator2 effectOp = resolveOperator(name);

		// create effect container
		GraphicalEffect effect = ClientGraphicalEffect.getInstance(source, target, duration, effectOp,
				cRemovalCallback);

		// store effect
		String id = Integer.toString(source.getEntityId());
		effects.put(id, effect);
	}

	/**
	 * Resolve Effect operator from name.
	 * 
	 * @param name effect name.
	 * 
	 * @return effect operator.
	 */
	Operator2 resolveOperator(String name) {

		// return effect for electrocute
		if (name.equals(Electrocute2.NAME)) {
			return ELECTROCUTE_OPERATOR;
		}

		// return default op.
		return DEFAULT_OPERATOR;
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
