package bassebombecraft.operator.conditional;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if one of the effect is active, i.e. OR.
 */
public class IfEitherEffectsAreActive implements Operator {

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
	 * Effect to test for.
	 */
	Effect effect2;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 * @param effect    effect to test for.
	 * @param effect2   effect to test for.
	 */
	public IfEitherEffectsAreActive(Supplier<LivingEntity> splEntity, Operator operator, Effect effect, Effect effect2) {
		this.splEntity = splEntity;
		this.operator = operator;
		this.effect = effect;
		this.effect2 = effect;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity entity = splEntity.get();

		// execute is effect is active
		if (entity.isPotionActive(effect)) {
			operator.run();
			return;
		}

		// execute is effect #2 is active
		if (entity.isPotionActive(effect2)) {
			operator.run();
		}
	}

}
