package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.isType;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the living entity is of the expected type.
 */
public class IsEntityOfType2 implements Operator2 {

	/**
	 * Type to test for.
	 */
	Class<?> type;

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetLivingEntity;
	
	/**
	 * Constructor.
	 * 
	 * @param fnGetLivingEntity function to get living entity.
	 * @param type type to test for.
	 */
	public IsEntityOfType2(Function<Ports, LivingEntity> fnGetLivingEntity, Class<?> type) {
		this.fnGetLivingEntity = fnGetLivingEntity;		
		this.type = type;		
	}

	@Override
	public Ports run(Ports ports) {
		// get entity 
		LivingEntity livingEntity = fnGetLivingEntity.apply(ports);
		
		// test
		if (isType(livingEntity, type))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
		
		return ports;
	}

}
