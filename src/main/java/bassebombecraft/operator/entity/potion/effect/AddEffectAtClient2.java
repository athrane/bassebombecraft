package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator2} interface which adds effect at
 * client side.
 */
public class AddEffectAtClient2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddEffectAtClient2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Function to get effect instance.
	 */
	Function<Ports, EffectInstance> fnGetEffecInstance1;

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #2 as target from ports.
	 * 
	 * Instance is configured to get effect instance #1 in the ports.
	 */
	public AddEffectAtClient2() {
		fnGetTarget = getFnGetLivingEntity2();
		fnGetEffecInstance1 = getFnEffectInstance1();
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);
		EffectInstance effectInstance = applyV(fnGetEffecInstance1, ports);

		// sync effect to client
		getProxy().getNetworkChannel().sendAddPotionEffectPacket(target, effectInstance);
	}

}
