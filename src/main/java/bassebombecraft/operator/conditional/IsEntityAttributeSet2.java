package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.*;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * Implementation of the {@linkplain Operator2} interface which which updates
 * the result port as successful if the living entity has custom attribute set to 1.0D.
 */
public class IsEntityAttributeSet2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetEntity;

	/**
	 * Entity attribute to test for.
	 */
	Attribute attribute;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get living entity.
	 * @param attribute   attribute to test for.
	 */
	public IsEntityAttributeSet2(Function<Ports, LivingEntity> fnGetEntity, Attribute attribute) {
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
	public IsEntityAttributeSet2(Attribute attribute) {
		this(getFnGetLivingEntity1(), attribute);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity livingEntity = applyV(fnGetEntity, ports);
		if (livingEntity == null) {
			ports.setResultAsFailed();
			return;
		}

		// exit as failed if attribute isn't set
		if (!isEntityAttributeSet(livingEntity, attribute)) {
			ports.setResultAsFailed();
			return;
		}

		// set as successful
		ports.setResultAsSucces();
	}

}
