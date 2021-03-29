package bassebombecraft.operator.entity;

import static bassebombecraft.entity.EntityUtils.setAttribute;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;

/**
 * Implementation of the {@linkplain Operator} interface which sets an entity attribute.
 */
public class AddAttribute implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddAttribute.class.getSimpleName();

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Entity attribute to set.
	 */
	Attribute attribute;

	/**
	 * Attribute value to set.
	 */
	double value;

	/**
	 * Constructor.
	 * 
	 * @param splEntity invoker entity supplier.
	 * @param attribute attribute to set.
	 * @param value     attribute value to set.
	 * 
	 */
	public AddAttribute(Supplier<LivingEntity> splEntity, Attribute attribute, double value) {
		this.splEntity = splEntity;
		this.attribute = attribute;
		this.value = value;
	}

	@Override
	public void run() {
		LivingEntity livingEntity = splEntity.get();
		setAttribute(livingEntity, attribute, value);
	}
}
