package bassebombecraft.client.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.operator.DefaultPorts;

/**
 * Implementation of {@linkplain ClientPorts}.
 */
public class DefaultClientPorts extends DefaultPorts implements ClientPorts {

	/**
	 * Matrix stack.
	 */
	MatrixStack matrixStack;

	/**
	 * Matrix stack #1 getter.
	 */
	static Function<ClientPorts, MatrixStack> fnGetMatrixStack1 = p -> p.getMatrixStack1();

	/**
	 * Matrix stack #1 setter.
	 */
	static BiConsumer<ClientPorts, MatrixStack> bcSetMatrixStack1 = (p, ms) -> p.setMatrixStack1(ms);

	/**
	 * Constructor
	 */
	DefaultClientPorts() {
		super();
	}

	@Override
	public MatrixStack getMatrixStack1() {
		return matrixStack;
	}

	@Override
	public ClientPorts setMatrixStack1(MatrixStack ms) {
		this.matrixStack = ms;
		return this;
	}

	public static Function<ClientPorts, MatrixStack> getFnMaxtrixStack1() {
		return fnGetMatrixStack1;
	}

	public static BiConsumer<ClientPorts, MatrixStack> getBcSetMaxtrixStack1() {
		return bcSetMatrixStack1;
	}

	/**
	 * Factory method.
	 * 
	 * @return ports.
	 */
	public static ClientPorts getInstance() {
		return new DefaultClientPorts();
	}

}
