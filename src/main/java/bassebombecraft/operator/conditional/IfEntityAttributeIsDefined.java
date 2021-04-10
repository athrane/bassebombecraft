package bassebombecraft.operator.conditional;

import java.util.function.Supplier;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if the entity attribute is defined.
 */
public class IfEntityAttributeIsDefined implements Operator {

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * Entity Attribute to test for.
	 */
	Attribute attribute;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 * @param attribute attribute to test for.
	 */
	public IfEntityAttributeIsDefined(Supplier<LivingEntity> splEntity, Operator operator, Attribute attribute) {
		this.splEntity = splEntity;
		this.operator = operator;
		this.attribute = attribute;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity entity = splEntity.get();

		// exit if attribute isn't defined
		if (!EntityUtils.hasAttribute(entity, attribute))
			return;

		operator.run();
	}

}
