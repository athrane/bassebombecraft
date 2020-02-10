package bassebombecraft.operator.conditional;

import static bassebombecraft.potion.PotionUtils.getEffectIfActive;

import java.util.Optional;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if the effect is active.
 */
public class IfEffectIsActive implements Operator {

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * Effect to test for.
	 */
	Effect effect;

	/**
	 * IfEffectIsActive constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 * @param effect    effect to test for.
	 */
	public IfEffectIsActive(Supplier<LivingEntity> splEntity, Operator operator, Effect effect) {
		this.splEntity = splEntity;
		this.operator = operator;
		this.effect = effect;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// exit if effect isn't active
		Optional<EffectInstance> optEffect = getEffectIfActive(livingEntity, effect);
		if (!optEffect.isPresent())
			return;

		operator.run();
	}

	/**
	 * Factory method.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 * @param effect    effect to test for.
	 * 
	 * @return operator instance.
	 */
	public static Operator getInstance(Supplier<LivingEntity> splEntity, Operator operator, Effect effect) {
		return new IfEffectIsActive(splEntity, operator, effect);
	}
}
