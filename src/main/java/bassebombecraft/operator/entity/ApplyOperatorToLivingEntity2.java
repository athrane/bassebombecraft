package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getBcSetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntities1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which applies embedded
 * operator to all living entities in the input set.
 * 
 * Each living entity in the input set is applied in sequence to the embedded
 * operator.
 * 
 * Any report of failed execution by the operator is ignored.
 */
public class ApplyOperatorToLivingEntity2 implements Operator2 {

	/**
	 * Embedded operator.
	 */
	Operator2 operator;

	/**
	 * Function to get living entities.
	 */
	Function<Ports, LivingEntity[]> fnGetLivingEntities;

	/**
	 * Function to set living entity in inner ports
	 */
	BiConsumer<Ports, LivingEntity> bcSetLivingEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetLivingEntities function to get living entities that the operator
	 *                            is applied to.
	 * @param bcSetLivingEntity   function to set living entity in ports.
	 * @param operator            operator which is executed.
	 */
	public ApplyOperatorToLivingEntity2(Function<Ports, LivingEntity[]> fnGetLivingEntities,
			BiConsumer<Ports, LivingEntity> bcSetLivingEntity, Operator2 operator) {
		this.fnGetLivingEntities = fnGetLivingEntities;
		this.bcSetLivingEntity = bcSetLivingEntity;
		this.operator = operator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance configured with living entity array #1 in the ports.
	 * 
	 * Instance configured with living entity #1 in the ports.
	 * 
	 * @param operator embedded operator which is executed.
	 */
	public ApplyOperatorToLivingEntity2(Operator2 operator) {
		this(getFnGetLivingEntities1(), getBcSetLivingEntity1(), operator);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity[] entities = applyV(fnGetLivingEntities, ports);
		for (LivingEntity entity : entities) {
			bcSetLivingEntity.accept(ports, entity);
			Operators2.run(ports, operator);
		}
	}

}
