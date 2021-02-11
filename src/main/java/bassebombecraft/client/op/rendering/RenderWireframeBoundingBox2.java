package bassebombecraft.client.op.rendering;

import static bassebombecraft.client.rendering.rendertype.RenderTypes.DEFAULT_LINES;
import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.ClientModConstants.DEFAULT_LINE_COLOR;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a wire
 * frame bounding box.
 * 
 * {@linkplain MatrixStack} is read from ports during execution.
 * 
 * {@linkplain AxisAlignedBB} is read from ports during execution.
 */
public class RenderWireframeBoundingBox2 implements Operator2 {


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
	 */
	public RenderWireframeBoundingBox2() {
		oscillateMax = 0.02F;
		color = DEFAULT_LINE_COLOR ;
		renderType = DEFAULT_LINES;
	}

	/**
	 * Constructor.
	 * 
	 * @param oscillateMax oscillate max value
	 * @param color        aabb color.
	 * @param renderType   render type for rendering the lines.
	 */
	public RenderWireframeBoundingBox2(float oscillateMax, Vector4f color, RenderType renderType) {
		this.oscillateMax = oscillateMax;
		this.color = color;
		this.renderType = renderType;
	}

	@Override
	public void run(Ports ports) {

		// get aabb
		AxisAlignedBB aabb = ports.getAabb();
		if (aabb == null)
			return;

		// get render buffer and builder
		Minecraft mcClient = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffer = mcClient.getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(renderType);

		// push matrix
		MatrixStack matrixStack = ports.getMatrixStack();
		if (matrixStack == null)
			return;
		matrixStack.push();

		// get position matrix
		Vec3d projectedView = mcClient.gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// grow aabb
		aabb = aabb.grow(oscillate(0, oscillateMax));

		// render
		renderWireframeBox(aabb, builder, positionMatrix);

		// restore matrix
		matrixStack.pop();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(renderType);
	}

	/**
	 * Render wireframe box.
	 * 
	 * @param aabb           AABB to render.
	 * @param builder        vertex builder
	 * @param positionMatrix position matrix.
	 */
	void renderWireframeBox(AxisAlignedBB aabb, IVertexBuilder builder, Matrix4f positionMatrix) {

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

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.pos(positionMatrix, x, y, z).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
	}

	void addVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z) {
		addVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
