package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.hasAttribute;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;

/**
 * Implementation of the {@linkplain Operator2} interface which which updates
 * the result port as successful if the living entity has attribute is defined.
 */
public class IsEntityAttributeDefined2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetEntity;

	/**
	 * Entity Attribute to test for.
	 */
	IAttribute attribute;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get living entity.
	 * @param attribute   attribute to test for.
	 */
	public IsEntityAttributeDefined2(Function<Ports, LivingEntity> fnGetEntity, IAttribute attribute) {
		this.fnGetEntity = fnGetEntity;
		this.attribute = attribute;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as entity from ports.
	 * 
	 * @param attribute attribute to test for.
	 */
	public IsEntityAttributeDefined2(IAttribute attribute) {
		this(getFnGetLivingEntity1(), attribute);
	}

	@Override
	public Ports run(Ports ports) {

		// exit as failed if no living entity is defined
		LivingEntity livingEntity = fnGetEntity.apply(ports);
		if (livingEntity == null) {
			ports.setResultAsFailed();
			return ports;
		}

		// exit as failed if attribute isn't defined
		if (!hasAttribute(livingEntity, attribute)) {
			ports.setResultAsFailed();
			return ports;
		}

		// set as successful
		ports.setResultAsSucces();
		return ports;
	}

}
