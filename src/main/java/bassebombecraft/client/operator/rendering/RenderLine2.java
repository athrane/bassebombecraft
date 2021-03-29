package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.DEFAULT_LINE_COLOR;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.DEFAULT_LINES;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a line.
 * 
 * Vector array are read from ports during execution. The first vector is
 * interpreted as the start of the line. Every subsequent entity is interpreted
 * as a line vertex.
 */
public class RenderLine2 implements Operator2 {

	/**
	 * Line color. Encoded as RGB+alpha
	 */
	Vector4f color;

	/**
	 * Render type.
	 */
	RenderType renderType;

	/**
	 * Function to get matrix stack.
	 */
	Function<ClientPorts, MatrixStack> fnGetMatrixStack;

	/**
	 * Function to get line vertexes.
	 */
	Function<Ports, Vector3d[]> fnGetLineVertexes;

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 */
	public RenderLine2() {
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.fnGetLineVertexes = getFnGetVectors1();
		this.color = DEFAULT_LINE_COLOR;
		this.renderType = DEFAULT_LINES;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 * 
	 * @param color      line color.
	 * @param renderType render type for rendering the line.
	 */
	public RenderLine2(Vector4f color, RenderType renderType) {
		this();
		this.color = color;
		this.renderType = renderType;
	}

	@Override
	public void run(Ports ports) {
		MatrixStack matrixStack = clientApplyV(fnGetMatrixStack, ports);
		Vector3d[] positions = applyV(fnGetLineVertexes, ports);

		// Get start and end position
		if (positions.length < 2)
			return;

		// get render buffer and builder
		Minecraft mcClient = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffer = mcClient.getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(renderType);

		// push matrix
		matrixStack.push();

		// get position matrix
		Vector3d projectedView = mcClient.gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// render
		for (int index = 0; index < (positions.length - 1); index++) {
			Vector3d start = positions[index];
			Vector3d end = positions[index + 1];
			renderLine(start, end, builder, positionMatrix);
		}

		// restore matrix
		matrixStack.pop();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(renderType);
	}

	/**
	 * Render line.
	 * 
	 * @param start          start position.
	 * @param end            end position.
	 * @param builder        vertex builder
	 * @param positionMatrix position matrix.
	 */
	void renderLine(Vector3d start, Vector3d end, IVertexBuilder builder, Matrix4f positionMatrix) {

		// AB
		addVertex(builder, positionMatrix, start.x, start.y, start.z);
		addVertex(builder, positionMatrix, end.x, end.y, end.z);
	}

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.pos(positionMatrix, x, y, z).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
	}

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z) {
		addVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
