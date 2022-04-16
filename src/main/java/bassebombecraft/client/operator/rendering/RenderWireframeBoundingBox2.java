package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.DEFAULT_LINE_COLOR;
import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.DEFAULT_LINES;
import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.operator.DefaultPorts.getFnAabb1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector4f;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a wire
 * frame bounding box.
 */
public class RenderWireframeBoundingBox2 implements Operator2 {

	/**
	 * Function to get aabb.
	 */
	Function<Ports, AABB> fnGetAabb;

	/**
	 * Function to get matrix stack.
	 */
	Function<ClientPorts, PoseStack> fnGetMatrixStack;

	/**
	 * oscillate max value.
	 */
	float oscillateMax;

	/**
	 * Bounding box color. Encoded as RGB+alpha
	 */
	Vector4f color;

	/**
	 * Render type.
	 */
	RenderType renderType;

	/**
	 * Constructor.
	 * 
	 * Instance is configured with AABB #1 from ports.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 */
	public RenderWireframeBoundingBox2() {
		this.fnGetAabb = getFnAabb1();
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.oscillateMax = 0.02F;
		this.color = DEFAULT_LINE_COLOR;
		this.renderType = DEFAULT_LINES;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with AABB #1 from ports.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * @param oscillateMax oscillate max value
	 * @param color        AABB color.
	 * @param renderType   render type for rendering the AABB lines.
	 */
	public RenderWireframeBoundingBox2(float oscillateMax, Vector4f color, RenderType renderType) {
		this();
		this.oscillateMax = oscillateMax;
		this.color = color;
		this.renderType = renderType;
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetAabb    function to get AABB.
	 * @param oscillateMax oscillate max value
	 * @param color        AABB color.
	 * @param renderType   render type for rendering the AABB lines.
	 */
	public RenderWireframeBoundingBox2(Function<Ports, AABB> fnGetAabb, float oscillateMax, Vector4f color,
			RenderType renderType) {
		this.fnGetAabb = fnGetAabb;
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.oscillateMax = oscillateMax;
		this.color = color;
		this.renderType = renderType;
	}

	
	@Override
	public void run(Ports ports) {
		AABB aabb = applyV(fnGetAabb, ports);
		PoseStack matrixStack = clientApplyV(fnGetMatrixStack, ports);

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

		// grow aabb
		aabb = aabb.inflate(oscillate(0, oscillateMax));

		// render
		renderWireframeBox(aabb, builder, positionMatrix);

		// restore matrix
		matrixStack.popPose();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.endBatch(renderType);
	}

	/**
	 * Render wireframe box.
	 * 
	 * @param aabb           AABB to render.
	 * @param builder        vertex builder
	 * @param positionMatrix position matrix.
	 */
	void renderWireframeBox(AABB aabb, VertexConsumer builder, Matrix4f positionMatrix) {

		// AB
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		// BC
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		// CD
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);

		// DA
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		// EF
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// FG
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// GH
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		// HE
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// AE
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// BF
		addVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// CG
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// DH
		addVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);

	}

	void addVertex(VertexConsumer builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.vertex(positionMatrix, x, y, z).color(color.x(), color.y(), color.z(), color.w()).endVertex();
	}

	void addVertex(VertexConsumer builder, Matrix4f positionMatrix, double x, double y, double z) {
		addVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
