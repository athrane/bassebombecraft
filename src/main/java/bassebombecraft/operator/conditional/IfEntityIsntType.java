package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.isType;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.world.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if the target entity ISN'T of the expected type.
 */
public class IfEntityIsntType implements Operator {

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * Type to test for.
	 */
	Class<?> type;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 * @param type      type to test for.
	 */
	public IfEntityIsntType(Supplier<LivingEntity> splEntity, Operator operator, Class<?> type) {
		this.splEntity = splEntity;
		this.operator = operator;
		this.type = type;
	}

	@Override
	public void run() {

		// exit if entity is of the type
		if (isType(splEntity.get(), type))
			return;

		operator.run();
	}

}
