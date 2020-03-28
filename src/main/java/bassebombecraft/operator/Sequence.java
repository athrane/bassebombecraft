package bassebombecraft.operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Implementation of the {@linkplain Operator} interface which executes three
 * embedded operators in sequence.
 */
public class Sequence implements Operator {

	/**
	 * Embedded operators.
	 */
	ArrayList<Operator> list = new ArrayList<Operator>();

	/**
	 * Constructor.
	 * 
	 * @param operator embedded operators which are executed in sequence
	 */
	public Sequence(Operator... operators) {
		Arrays.stream(operators).collect(Collectors.toCollection(ArrayList<Operator>::new));
	}

	@Override
	public void run() {
		list.forEach(o -> o.run());
	}

}
