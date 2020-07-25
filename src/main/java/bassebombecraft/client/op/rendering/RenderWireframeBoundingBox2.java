package bassebombecraft.client.op.rendering;

import static bassebombecraft.geom.GeometryUtils.oscillate;

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
 */
public class RenderWireframeBoundingBox2 implements Operator2 {

	/**
	 * Default color.
	 */
	static final Vector4f WHITE_COLOR = new Vector4f(1, 1, 1, 1);

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
		color = WHITE_COLOR;
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
	public Ports run(Ports ports) {

		// get render buffer and builder
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(renderType);

		// push matrix
		MatrixStack matrixStack = ports.getMatrixStack();
		matrixStack.push();

		// get position matrix
		Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// grow aabb
		AxisAlignedBB aabb = ports.getAabb();
		aabb = aabb.grow(oscillate(0, oscillateMax));

		// render
		renderWireframeBox(aabb, builder, positionMatrix);

		// restore matrix
		matrixStack.pop();

		// Rendering bug, see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(renderType);

		return ports;
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
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		// BC
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		// CD
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);

		// DA
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		// EF
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// FG
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// GH
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		// HE
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// AE
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// BF
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// CG
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// DH
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);

	}

	void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.pos(positionMatrix, x, y, z).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
	}

	void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z) {
		addWhiteVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
