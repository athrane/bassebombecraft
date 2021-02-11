package bassebombecraft.operator;

/**
 * Unchecked exception thrown by the Operators2.validateNotNull(..) method when
 * a input parameter to an operator isn't defined.
 */
public class UndefinedOperatorInputException extends RuntimeException {

	/**
	 * Serial version ID.
	 */
	static final long serialVersionUID = -9054813773510775487L;

	/**
	 * Constructs a {@code UndefinedReferenceException} with no detail message.
	 */
	public UndefinedOperatorInputException() {
		super();
	}

	/**
	 * Constructs a {@code UndefinedReferenceException} with the specified detail
	 * message.
	 *
	 * @param s the detail message.
	 */
	public UndefinedOperatorInputException(String s) {
		super(s);
	}

}
