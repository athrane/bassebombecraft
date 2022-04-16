package bassebombecraft.client.operator.rendering;

import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.DEFAULT_LINES;
import static bassebombecraft.operator.DefaultPorts.getFnGetColor4f1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.color.Color4f;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a line
 * with dynamic color.
 * 
 * A vector array is read from ports during execution. The first vector is
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
	Function<ClientPorts, PoseStack> fnGetMatrixStack;

	/**
	 * Function to get line vertexes.
	 */
	Function<Ports, Vec3[]> fnGetLineVertexes;

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
		PoseStack matrixStack = clientApplyV(fnGetMatrixStack, ports);
		Vec3[] positions = applyV(fnGetLineVertexes, ports);
		Color4f color = applyV(fnGetColor, ports);

		// Get start and end position
		if (positions.length < 2)
			return;

		// get render buffer and builder
		Minecraft mcClient = Minecraft.getInstance();
		MultiBufferSource.BufferSource buffer = mcClient.renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(renderType);

		// push matrix
		matrixStack.pushPose();

		// get position matrix
		Vec3 projectedView = mcClient.gameRenderer.getMainCamera().getPosition();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.last().pose();

		// render
		for (int index = 0; index < (positions.length - 1); index++) {
			Vec3 start = positions[index];
			Vec3 end = positions[index + 1];
			renderLine(start, end, builder, positionMatrix, color);
		}

		// restore matrix
		matrixStack.popPose();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.endBatch(renderType);
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
	void renderLine(Vec3 start, Vec3 end, VertexConsumer builder, Matrix4f positionMatrix, Color4f color) {

		// AB
		addVertex(builder, positionMatrix, start.x, start.y, start.z, color);
		addVertex(builder, positionMatrix, end.x, end.y, end.z, color);
	}

	void addVertex(VertexConsumer builder, Matrix4f positionMatrix, double x, double y, double z, Color4f color) {
		addVertex(builder, positionMatrix, (float) x, (float) y, (float) z, color);
	}

	void addVertex(VertexConsumer builder, Matrix4f positionMatrix, float x, float y, float z, Color4f color) {
		builder.vertex(positionMatrix, x, y, z).color(color.getR(), color.getG(), color.getB(), color.getAlpha())
				.endVertex();
	}

}
