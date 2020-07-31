package bassebombecraft.client.rendering.entity;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.TEXTURE_PATH;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.projectile.CompositeProjectileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Client side renderer for projectile {@linkplain CompositeProjectileEntity}.
 */
public class CompositeProjectileEntityRenderer extends EntityRenderer<CompositeProjectileEntity> {

	// static final float SCALE = 0.05625F;
	static final float SCALE = 0.05F;

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = new ResourceLocation(MODID, TEXTURE_PATH + "compositeprojectileentity.png");

	/**
	 * Constructor
	 * 
	 * @param renderManager render manager.
	 */
	public CompositeProjectileEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(CompositeProjectileEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {

		matrixStackIn.push();

		// rotate with player
		matrixStackIn.rotate(Vector3f.YP
				.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
		matrixStackIn.rotate(Vector3f.ZP
				.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));

		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));		
		matrixStackIn.scale(SCALE, SCALE, SCALE);

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutout(TEXTURE));
		MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
		Matrix4f matrix4f = matrixstack$entry.getPositionMatrix(); // getMatrix();
		Matrix3f matrix3f = matrixstack$entry.getNormalMatrix(); // getNormal();

		for (int j = 0; j < 4; ++j) {
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 10, 10, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -10, 10, 0, 1, 0.0F, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -10, -10, 0, 1, 1, 0, 1, 0, packedLightIn);
			this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 10, -10, 0, 0.0F, 1, 0, 1, 0, packedLightIn);
		}

		matrixStackIn.pop();

		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	public void drawTexture(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder vertexBuilder, int x, int y, int z,
			float u, float v, int normalX, int normalZ, int normalY, int lightmapUV) {

		vertexBuilder.pos(matrix4f, (float) x, (float) y, (float) z)
			.color(255, 255, 255, 255)
			.tex(u, v)
			.overlay(OverlayTexture.DEFAULT_LIGHT).lightmap(lightmapUV)
			.normal(matrix3f, (float) normalX, (float) normalY, (float) normalZ)
			.endVertex();
	}

	@Override
	protected int getBlockLight(CompositeProjectileEntity entityIn, float partialTicks) {
		return 15;
	}

	@Override
	public ResourceLocation getEntityTexture(CompositeProjectileEntity entity) {
		return TEXTURE;
	}

}