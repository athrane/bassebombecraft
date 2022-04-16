package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import static bassebombecraft.entity.EntityUtils.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * Implementation of the {@linkplain Operator2} interface which sets the value of an entity
 * attribute.
 */
public class SetEntityAttribute2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SetEntityAttribute2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

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
	 * @param fnGetTarget function to get target entity.
	 * @param attribute   attribute to set.
	 * @param value       attribute value to set.
	 */
	public SetEntityAttribute2(Function<Ports, LivingEntity> fnGetTarget, Attribute attribute, double value) {
		this.fnGetTarget = fnGetTarget;
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as target from ports.
	 * 
	 * @param attribute attribute to set.
	 * @param value     attribute value to set.
	 */
	public SetEntityAttribute2(Attribute attribute, double value) {
		this(getFnGetLivingEntity1(), attribute, value);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);
		setAttribute(target, attribute, value);
	}

}
