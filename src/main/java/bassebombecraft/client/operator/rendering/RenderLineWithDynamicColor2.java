package bassebombecraft.client.operator.rendering;

import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.DEFAULT_LINES;
import static bassebombecraft.operator.DefaultPorts.getFnGetColor4f1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.color.Color4f;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a line
 * with dynamic color.
 * 
 * Vector array are read from ports during execution. The first vector is
 * interpreted as the start of the line. Every subsequent entity is interpreted
 * as a line vertex.
 */
public class RenderLineWithDynamicColor2 implements Operator2 {

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
	Function<Ports, Vec3d[]> fnGetLineVertexes;

	/**
	 * Function to get color.
	 */
	Function<Ports, Color4f> fnGetColor;

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 * 
	 * Instance is configured with vector4f #1 as color from ports.
	 */
	public RenderLineWithDynamicColor2() {
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.fnGetLineVertexes = getFnGetVectors1();
		this.fnGetColor = getFnGetColor4f1();
		this.renderType = DEFAULT_LINES;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 * 
	 * @param fnGetColor function to get color.
	 * @param renderType render type for rendering the line.
	 */
	public RenderLineWithDynamicColor2(Function<Ports, Color4f> fnGetColor, RenderType renderType) {
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.fnGetLineVertexes = getFnGetVectors1();
		this.fnGetColor = fnGetColor;
		this.renderType = renderType;
	}

	@Override
	public void run(Ports ports) {
		MatrixStack matrixStack = clientApplyV(fnGetMatrixStack, ports);
		Vec3d[] positions = applyV(fnGetLineVertexes, ports);
		Color4f color = applyV(fnGetColor, ports);

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
		Vec3d projectedView = mcClient.gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// render
		for (int index = 0; index < (positions.length - 1); index++) {
			Vec3d start = positions[index];
			Vec3d end = positions[index + 1];
			renderLine(start, end, builder, positionMatrix, color);
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
	 * @param color          line color as RGB+alpha
	 */
	void renderLine(Vec3d start, Vec3d end, IVertexBuilder builder, Matrix4f positionMatrix, Color4f color) {

		// AB
		addVertex(builder, positionMatrix, start.x, start.y, start.z, color);
		addVertex(builder, positionMatrix, end.x, end.y, end.z, color);
	}

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z, Color4f color) {
		addVertex(builder, positionMatrix, (float) x, (float) y, (float) z, color);
	}

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z, Color4f color) {
		builder.pos(positionMatrix, x, y, z).color(color.getR(), color.getG(), color.getB(), color.getAlpha())
				.endVertex();
	}

}
