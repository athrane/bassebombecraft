package bassebombecraft.operator;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operators in sequence.
 * 
 * @deprecated Replace with version 2 of the framework and use {@linkplain Sequence2}.
 */
@Deprecated
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
