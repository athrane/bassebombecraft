package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.CONTAGION_LINE_COLOR1;
import static bassebombecraft.ClientModConstants.DEFAULT_LINE_COLOR;
import static bassebombecraft.ClientModConstants.LIGHTNING_LINE_COLOR1;
import static bassebombecraft.ClientModConstants.LIGHTNING_LINE_COLOR2;
import static bassebombecraft.ClientModConstants.PROJECTILE_TRAIL_LINE_COLOR1;
import static bassebombecraft.ClientModConstants.PROJECTILE_TRAIL_LINE_COLOR2;
import static bassebombecraft.ClientModConstants.WILDFIRE_LINE_COLOR1;
import static bassebombecraft.ClientModConstants.*;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.*;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.LIGHTNING_LINES;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.PROJECTILE_TRAIL_LINES;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.SIMPLE_LINES;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.WILDFIRE_LINES;
import static bassebombecraft.operator.DefaultPorts.getBcSetColor4f1;
import static bassebombecraft.operator.DefaultPorts.getFnGetColor4f1;
import static bassebombecraft.operator.DefaultPorts.getFnGetInteger1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import bassebombecraft.client.operator.rendering.InitColor2;
import bassebombecraft.client.operator.rendering.InitContagion2;
import bassebombecraft.client.operator.rendering.InitElectrocute2;
import bassebombecraft.client.operator.rendering.InitLineRenderingFromPorts2;
import bassebombecraft.client.operator.rendering.InitProjectileTrailRendering2;
import bassebombecraft.client.operator.rendering.InitWildfire2;
import bassebombecraft.client.operator.rendering.RenderLine2;
import bassebombecraft.client.operator.rendering.RenderLineWithDynamicColor2;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.operator.NullOp2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.world.entity.Entity;

/**
 * CLIENT side implementation of the {@linkplain GraphicalEffectRepository}
 * interface.
 * 
 * Effects are identified by effect hash code so any effect is accepted.
 */
public class ClientGraphicalEffectRepository implements GraphicalEffectRepository {

	/**
	 * Default effect operator.
	 */
	final static Operator2 DEFAULT_OPERATOR = new NullOp2();

	/**
	 * Effect operator for electrocute effect.
	 */
	final static Operator2 ELECTROCUTE_OPERATOR = new Sequence2(new InitElectrocute2(),
			new InitColor2(getBcSetColor4f1(), LIGHTNING_LINE_COLOR1, LIGHTNING_LINE_COLOR2),
			new RenderLineWithDynamicColor2(getFnGetColor4f1(), LIGHTNING_LINES));

	/**
	 * Effect operator for projectile trail effect.
	 */
	final static Operator2 PROJECTILE_TRAIL_OPERATOR = new Sequence2(new InitProjectileTrailRendering2(),
			new InitColor2(getBcSetColor4f1(), PROJECTILE_TRAIL_LINE_COLOR1, PROJECTILE_TRAIL_LINE_COLOR2),
			new RenderLineWithDynamicColor2(getFnGetColor4f1(), PROJECTILE_TRAIL_LINES));

	final static Function<Ports, Integer> fnGetMax = getFnGetInteger1();

	/**
	 * Effect operator for wildfire effect.
	 */
	final static Operator2 WILDFIRE_OPERATOR = new Sequence2(new InitWildfire2(),
			new InitColor2(getBcSetColor4f1(), WILDFIRE_LINE_COLOR1, WILDFIRE_LINE_COLOR2),
			new RenderLineWithDynamicColor2(getFnGetColor4f1(), WILDFIRE_LINES));

	/**
	 * Effect operator for contagion effect.
	 */
	final static Operator2 CONTAGION_OPERATOR = new Sequence2(new InitContagion2(),
			new InitColor2(getBcSetColor4f1(), CONTAGION_LINE_COLOR1, CONTAGION_LINE_COLOR1),
			new RenderLineWithDynamicColor2(getFnGetColor4f1(), CONTAGION_LINES));
	
	/**
	 * Effect operator for line effect.
	 */
	final static Operator2 LINE_OPERATOR = new Sequence2(new InitLineRenderingFromPorts2(),
			new RenderLine2(DEFAULT_LINE_COLOR, SIMPLE_LINES));

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
	public void add(Entity source, Entity target, int duration, Effect effect) {

		// resolve effect operator
		Operator2 effectOp = resolveOperator(effect);

		// create effect container
		GraphicalEffect effectObject = ClientGraphicalEffect.getInstance(source, target, duration, effectOp);

		// exit if effect exits
		if (contains(effectObject.getId()))
			return;

		// register effect with client duration repository
		DurationRepository repository = getProxy().getClientDurationRepository();
		repository.add(effectObject.getId(), duration, cRemovalCallback);

		// store effect
		effects.put(effectObject.getId(), effectObject);
	}

	@Override
	public void addUnresolvedSource(int sourceId, Entity target, int duration, Effect effect) {

		// resolve effect operator
		Operator2 effectOp = resolveOperator(effect);

		// create effect container
		GraphicalEffect effectObject = ClientGraphicalEffect.getInstance(sourceId, target, duration, effectOp);

		// exit if effect exits
		if (contains(effectObject.getId()))
			return;

		// register effect with client duration repository
		DurationRepository repository = getProxy().getClientDurationRepository();
		repository.add(effectObject.getId(), duration, cRemovalCallback);

		// store effect
		effects.put(effectObject.getId(), effectObject);

	}

	/**
	 * Resolve operator from effect.
	 * 
	 * @param effect effect to resolve operator from.
	 * 
	 * @return effect operator.
	 */
	Operator2 resolveOperator(Effect effect) {
		switch (effect) {

		case NO_EFFECT:
			return DEFAULT_OPERATOR;

		case LINE:
			return LINE_OPERATOR;

		case ELECTROCUTE:
			return ELECTROCUTE_OPERATOR;

		case PROJECTILE_TRAIL:
			return PROJECTILE_TRAIL_OPERATOR;

		case WILDFIRE:
			return WILDFIRE_OPERATOR;

		case CONTAGION:
			return CONTAGION_OPERATOR;

		default:
			return DEFAULT_OPERATOR;
		}
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
