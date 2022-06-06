package bassebombecraft.client.rendering.entity;

import static net.minecraft.util.Mth.lerp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

/**
 * Client side renderer for generic projectiles
 * {@linkplain GenericCompositeProjectileEntity}.
 * 
 * This is a abstract super class for actual renderer implementations.
 */
public abstract class GenericCompositeProjectileEntityRenderer<T extends Entity> extends EntityRenderer<T> {

	/**
	 * Projectile scale.
	 */
	static final float SCALE = 0.05F;

	/**
	 * Constructor
	 * 
	 * @param renderManager render manager.
	 */
	public GenericCompositeProjectileEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStackIn,
			MultiBufferSource bufferIn, int packedLightIn) {

		matrixStackIn.pushPose();

		// rotate with player
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(lerp(partialTicks, entity.xRotO, entity.getXRot())));

		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		matrixStackIn.scale(SCALE, SCALE, SCALE);

		VertexConsumer ivertexbuilder = bufferIn.getBuffer(entityCutout(getTextureLocation(entity)));
		Pose matrixstack$entry = matrixStackIn.last();
		Matrix4f matrix4f = matrixstack$entry.pose();
		Matrix3f matrix3f = matrixstack$entry.normal();

		for (int j = 0; j < 4; ++j) {
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 10, 10, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -10, 10, 0, 1, 0.0F, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -10, -10, 0, 1, 1, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 10, -10, 0, 0.0F, 1, 0, 1, 0, packedLightIn);
		}
		matrixStackIn.popPose();

		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	 * draw texture
	 * 
	 * @param matrix4f
	 * @param matrix3f
	 * @param vertexBuilder
	 * @param x
	 * @param y
	 * @param z
	 * @param u
	 * @param v
	 * @param normalX
	 * @param normalZ
	 * @param normalY
	 * @param lightmapUV
	 */
	public void drawTexture(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexBuilder, int x, int y, int z,
			float u, float v, int normalX, int normalZ, int normalY, int lightmapUV) {

		vertexBuilder.vertex(matrix4f, (float) x, (float) y, (float) z).color(255, 255, 255, 255).uv(u, v)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmapUV)
				.normal(matrix3f, (float) normalX, (float) normalY, (float) normalZ).endVertex();
	}

	@Override
	protected int getBlockLightLevel(T entityIn, BlockPos pos) {
		return 15;
	}

}
