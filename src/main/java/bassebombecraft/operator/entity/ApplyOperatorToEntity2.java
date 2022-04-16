package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;

/**
 * Implementation of the {@linkplain Operator2} interface which applies embedded operator to
 * all entities in the input set.
 * 
 * Each entity in the input set is applied in sequence to the embedded operator.
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
	Function<Ports, Entity[]> fnGetEntities;

	/**
	 * Function to set entity in inner ports
	 */
	BiConsumer<Ports, Entity> bcSetEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntities function to get entities that the operator is applied
	 *                       to.
	 * @param bcSetEntity   function to set entity in ports.
	 * @param operator       operator which is executed.
	 */
	public ApplyOperatorToEntity2(Function<Ports, Entity[]> fnGetEntities, BiConsumer<Ports, Entity> bcSetEntity,
			Operator2 operator) {
		this.fnGetEntities = fnGetEntities;
		this.bcSetEntity = bcSetEntity;
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
		Entity[] entities = applyV(fnGetEntities, ports);
		for (Entity entity : entities) {
			bcSetEntity.accept(ports, entity);
			Operators2.run(ports, operator);
		}
	}

}
