package bassebombecraft.operator.conditional;

import static bassebombecraft.potion.PotionUtils.isEffectActive;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

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
		if (!isEffectActive(livingEntity, effect))
			return;

		operator.run();
	}

}
