package bassebombecraft.operator.conditional;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the effect is active.
 */
public class IsEffectActive2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetEntity;

	/**
	 * Effect to test for.
	 */
	MobEffect effect;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get living entity.
	 * @param effect      effect to test for.
	 */
	public IsEffectActive2(Function<Ports, LivingEntity> fnGetEntity, MobEffect effect) {
		this.fnGetEntity = fnGetEntity;
		this.effect = effect;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as entity from ports.
	 * 
	 * @param effect effect to test for.
	 */
	public IsEffectActive2(MobEffect effect) {
		this(getFnGetLivingEntity1(), effect);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity entity = applyV(fnGetEntity, ports);

		if (entity.hasEffect(effect))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
	}

}
