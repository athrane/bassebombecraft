package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;

/**
 * Implementation of the {@linkplain Operator2} interface which applies all
 * entity object from the input set to embedded operator.
 * 
 * Each entity in the input set are applied in sequence to operator.
 * 
 * Any report of failed execution by the operator is ignored.
 */
public class ApplyOperatorToEntity2 implements Operator2 {

	/**
	 * Embedded operator.
	 */
	Operator2 operator;

	/**
	 * Function to get entities.
	 */
	Function<Ports, Entity[]> fnGetEntities1;

	/**
	 * Function to set entity in inner ports
	 */
	BiConsumer<Ports, Entity> bcSetEntity1;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntities1 function to get entities that the operator is applied
	 *                       to.
	 * @param bcSetEntity1   function to set entity in ports.
	 * @param operator       operator which is executed.
	 */
	public ApplyOperatorToEntity2(Function<Ports, Entity[]> fnGetEntities1, BiConsumer<Ports, Entity> bcSetEntity1,
			Operator2 operator) {
		this.fnGetEntities1 = fnGetEntities1;
		this.bcSetEntity1 = bcSetEntity1;
		this.operator = operator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance configured with entity array #1 in the ports.
	 * 
	 * Instance configured with entity #1 in the ports.
	 * 
	 * @param operator embedded operator which is executed.
	 */
	public ApplyOperatorToEntity2(Operator2 operator) {
		this(getFnGetEntities1(), getBcSetEntity1(), operator);
	}

	@Override
	public void run(Ports ports) {
		Entity[] entities = applyV(fnGetEntities1, ports);
		for (Entity entity : entities) {
			bcSetEntity1.accept(ports, entity);
			Operators2.run(ports, operator);
		}
	}

}
