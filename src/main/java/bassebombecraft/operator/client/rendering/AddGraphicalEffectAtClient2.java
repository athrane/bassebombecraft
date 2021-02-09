package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnGetDouble1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;

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
	 * Effect name.
	 */
	String name;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource   function to get source entity in effect.
	 * @param fnGetTarget   function to get target entity in effect.
	 * @param fnGetDuration function to get duration (in game ticks).
	 * @param name          effect name.
	 */
	public AddGraphicalEffectAtClient2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
			Function<Ports, Double> fnGetDuration, String name) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.fnGetDuration = fnGetDuration;
		this.name = name;
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
	 * @param name effect name.
	 */
	public AddGraphicalEffectAtClient2(String name) {
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetDouble1(), name);
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
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetDouble1(), "default");
	}

	@Override
	public Ports run(Ports ports) {
		
		// get source		
		Entity source = fnGetSource.apply(ports);
		if (source == null)
			return ports;
		
		// get target		
		Entity target = fnGetTarget.apply(ports);
		if (target == null)
			return ports;
		
		int duration = fnGetDuration.apply(ports).intValue();
		getProxy().getNetworkChannel().sendAddGraphicalEffectPacket(source, target, duration, name);
		return ports;
	}
}
