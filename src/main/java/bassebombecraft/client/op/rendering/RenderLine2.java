package bassebombecraft.client.op.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a line.
 * 
 * {@linkplain MatrixStack} is read from ports during execution.
 * 
 * Vector array are read from ports during execution. The first vector is
 * interpreted as the start of the line. Every subsequent entity is interpreted
 * as a line segment.
 */
public class RenderLine2 implements Operator2 {

	/**
	 * Default color.
	 */
	static final Vector4f WHITE_COLOR = new Vector4f(1, 1, 1, 1);

	/**
	 * Line color. Encoded as RGB+alpha
	 */
	Vector4f color;

	/**
	 * Render type.
	 */
	RenderType renderType;

	/**
	 * Constructor.
	 */
	public RenderLine2() {
		color = WHITE_COLOR;
	}

	/**
	 * Constructor.
	 * 
	 * @param color      line color.
	 * @param renderType render type for rendering the line.
	 */
	public RenderLine2(Vector4f color, RenderType renderType) {
		this.color = color;
		this.renderType = renderType;
	}

	@Override
	public Ports run(Ports ports) {

		// get vectors
		Vec3d[] vectors = ports.getVectors1();
		if (vectors == null)
			return ports;

		// Get start and end position
		if (vectors.length < 2)
			return ports;
		Vec3d start = vectors[0];
		Vec3d end = vectors[1];

		// get render buffer and builder
		Minecraft mcClient = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffer = mcClient.getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(renderType);

		// push matrix
		MatrixStack matrixStack = ports.getMatrixStack();
		if (matrixStack == null)
			return ports;
		matrixStack.push();

		// get position matrix
		Vec3d projectedView = mcClient.gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// render
		renderLine(start, end, builder, positionMatrix);

		// restore matrix
		matrixStack.pop();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(renderType);

		return ports;
	}

	/**
	 * Render line.
	 * 
	 * @param start          start position.
	 * @param end            end position.
	 * @param builder        vertex builder
	 * @param positionMatrix position matrix.
	 */
	void renderLine(Vec3d start, Vec3d end, IVertexBuilder builder, Matrix4f positionMatrix) {

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
