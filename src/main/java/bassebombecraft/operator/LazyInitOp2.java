package bassebombecraft.operator;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Operator can lazily resolve operator from function interface.
 * 
 * Its purpose is to support lazy initialisation of sequences of operators while
 * avoiding class loading issues due to configuration properties not have been
 * loaded.
 */
public class LazyInitOp2 implements Operator2 {

	/**
	 * Optional which holds resolved operator.
	 */
	Optional<Operator2> optOp = Optional.empty();

	/**
	 * Function which can resolve operator.
	 */
	Supplier<Operator2> splOp = null;

	/**
	 * Constructor.
	 * 
	 * @param splOp function to initialize operator.
	 */
	LazyInitOp2(Supplier<Operator2> splOp) {
		this.splOp = splOp;
	}

	/**
	 * Resolves operator.
	 * 
	 * If operator isn't result then the initializer function is invoked and the
	 * operator is stored. If the operator was resolved the the instance is
	 * returned.
	 * 
	 * @return return resolved operator.
	 */
	Operator2 resolveOperator() {
		if (optOp.isPresent())
			return optOp.get();
		optOp = Optional.of(splOp.get());
		return optOp.get();
	}

	@Override
	public void run(Ports ports) {
		Operators2.run(ports, resolveOperator());
	}

	/**
	 * Factory method.
	 * 
	 * @param splOp function to initialize operator.
	 * 
	 * @return operator instance which supports lazy initialize if operator.
	 */
	public static LazyInitOp2 of(Supplier<Operator2> splOp) {
		return new LazyInitOp2(splOp);
	}

}
