package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.NO_EFFECT;
import static bassebombecraft.operator.DefaultPorts.getFnGetDouble1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;

/**
 * Implementation of the {@linkplain Operator2} interface which adds a graphical
 * effect at client side.
 */
public class AddGraphicalEffectAtClient2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddGraphicalEffectAtClient2.class.getSimpleName();

	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, Entity> fnGetTarget;

	/**
	 * Function to get duration.
	 */
	Function<Ports, Double> fnGetDuration;

	/**
	 * Graphical effect.
	 */
	Effect effect;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource   function to get source entity in effect.
	 * @param fnGetTarget   function to get target entity in effect.
	 * @param fnGetDuration function to get duration (in game ticks).
	 * @param effect        graphical effect.
	 */
	public AddGraphicalEffectAtClient2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
			Function<Ports, Double> fnGetDuration, Effect effect) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.fnGetDuration = fnGetDuration;
		this.effect = effect;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source entity from ports.
	 * 
	 * Instance is configured with entity #2 as target entity from ports.
	 * 
	 * Instance is configured with double #1 as effect duration from ports.
	 * 
	 * @param effect graphical effect.
	 */
	public AddGraphicalEffectAtClient2(Effect effect) {
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetDouble1(), effect);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source entity from ports.
	 * 
	 * Instance is configured with entity #2 as target entity from ports.
	 * 
	 * Instance is configured with double #1 as effect duration from ports.
	 */
	public AddGraphicalEffectAtClient2() {
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetDouble1(), NO_EFFECT);
	}

	@Override
	public void run(Ports ports) {
		Entity source = applyV(fnGetSource, ports);
		Entity target = applyV(fnGetTarget, ports);
		int duration = applyV(fnGetDuration, ports).intValue();
		
		getProxy().getNetworkChannel().sendAddGraphicalEffectPacket(source, target, duration, effect);
	}
}
