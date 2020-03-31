package bassebombecraft.operator;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operators in sequence.
 */
public class Sequence implements Operator {

	/**
	 * Embedded operators.
	 */
	List<Operator> operatorList;

	/**
	 * Constructor.
	 * 
	 * @param operators embedded operators which are executed in sequence
	 */
	public Sequence(Operator... operators) {
		operatorList = Arrays.asList(operators);
	}

	@Override
	public void run() {
		operatorList.forEach(o -> o.run());
	}

}
