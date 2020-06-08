package bassebombecraft.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;

/**
 * Class for execution of operator.
 */
public class Operators2 {

	/**
	 * Execute operator.
	 * 
	 * @param ports   input ports
	 * @param opertor operator to execute.
	 * 
	 * @return ports. If execution fails then the original ports is returned.
	 */
	public static Ports run(Ports ports, Operator2 operator) {
		try {
			return operator.run(ports);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
			return ports;
		}
	}

	/**
	 * Execute operators.
	 * 
	 * Operators are executed in sequence. If an executed operator set the result
	 * port to failed (i.e. failed) then the execution of the sequence is aborted.
	 * 
	 * @param ports   input ports
	 * @param opertor operators executed in sequence.
	 * 
	 * @return ports. 
	 */
	public static Ports run(Ports ports, Operator2... operators) {
		Stream.of(operators).forEach( op -> {
			if(!ports.getResult()) return;			
			Operators2.run(ports, op);	
		});
		return ports;		
	}

	/**
	 * Execute operators.
	 * 
	 * Operators are executed in sequence. If an executed operator set the result
	 * port to failed (i.e. failed) then the execution of the sequence is aborted.
	 * 
	 * @param ports   input ports
	 * @param opertor operators executed in sequence.
	 * 
	 * @return ports. 
	 */
	public static Ports run(LivingEntity livingEntity, Operator2... operators) {
		Ports ports = getInstance(livingEntity);
		return run(ports, operators);
	}
	
}
