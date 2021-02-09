package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which adds a graphical
 * effect at client side.
 * 
 */
public class AddGraphicalEffectAtClient2 implements Operator2 {

	/**
	 * Function to get source entity.
	 */
	Function<Ports, LivingEntity> fnGetSource;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Effect duration (in game ticks).
	 */
	int duration;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity in effect.
	 * @param fnGetTarget function to get target entity in effect.
	 * @param duration effect duration (in game ticks).
	 */
	public AddGraphicalEffectAtClient2(Function<Ports, LivingEntity> fnGetSource,
			Function<Ports, LivingEntity> fnGetTarget, int duration) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.duration = duration;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as source entity from ports.
	 * 
	 * Instance is configured with living entity #2 as target entity from ports.
	 */
	public AddGraphicalEffectAtClient2() {
		this.fnGetSource = getFnGetLivingEntity1();
		this.fnGetSource = getFnGetLivingEntity2();
		this.duration = 10;
	}

	@Override
	public Ports run(Ports ports) {
		LivingEntity source = fnGetSource.apply(ports);
		LivingEntity target = fnGetTarget.apply(ports);
		getProxy().getNetworkChannel().sendAddGraphicalEffectPacket(source, target, duration);
		
		getBassebombeCraft().getLogger().debug("AddGraphicalEffectAtClient2 source="+source);
		getBassebombeCraft().getLogger().debug("AddGraphicalEffectAtClient2 target="+target);				
		return ports;
	}
}
