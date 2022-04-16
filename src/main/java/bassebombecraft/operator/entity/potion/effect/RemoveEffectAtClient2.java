package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;

/**
 * Implementation of the {@linkplain Operator2} interface which removes effect
 * at client side.
 */
public class RemoveEffectAtClient2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = RemoveEffectAtClient2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Effect.
	 */
	MobEffect effect;

	/**
	 * Constructor.
	 * 
	 * @param fnGetTarget function to get target entity.
	 * @param effect      effect.
	 */
	public RemoveEffectAtClient2(Function<Ports, LivingEntity> fnGetTarget, MobEffect effect) {
		this.fnGetTarget = fnGetTarget;
		this.effect = effect;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as target from ports.
	 * 
	 * @param effect effect.
	 */
	public RemoveEffectAtClient2(MobEffect effect) {
		this(getFnGetLivingEntity1(), effect);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);

		// sync effect to client
		getProxy().getNetworkChannel().sendRemoveEffectPacket(target, effect);
	}

}
