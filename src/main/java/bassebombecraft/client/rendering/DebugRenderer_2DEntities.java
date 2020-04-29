package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static net.minecraft.client.renderer.entity.LivingRenderer.getPackedOverlay;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 */
public class DebugRenderer_2DEntities {

	public static void renderPre(RenderLivingEvent.Pre event) {

		event.setCanceled(true); // disable normal rendering
		
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();

		LivingEntity entity = event.getEntity();
		LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = event.getRenderer();
		float partialTicks = event.getPartialRenderTick();

		try {
			float f = MathHelper.interpolateAngle(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
			float f1 = MathHelper.interpolateAngle(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead);
			float f2 = f1 - f;

			// skipping handling of riding entities

			float f6 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);

			// skipping handling of sleeping entities

			float f7 = handleRotationFloat(entity, partialTicks);
			applyRotations(entity, matrixStack, f7, f, partialTicks);

			double x = entity.getPosX();
			double z = entity.getPosZ();			
			prepareFlatRender(matrixStack, x, z, f);
			
			matrixStack.scale(-1.0F, -1.0F, 1.0F);
			// this.preRenderCallback(entity, matrixStack, partialTicks);
			matrixStack.translate(0.0D, (double) -1.501F, 0.0D);

			float f8 = 0.0F;
			float f5 = 0.0F;

			EntityModel<LivingEntity> entityModel = renderer.getEntityModel();
			entityModel.setLivingAnimations(entity, f5, f8, partialTicks);
			entityModel.render(entity, f5, f8, f7, f2, f6);

			boolean flag = isVisible(entity);
			boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
			RenderType rendertype = getRenderType(entity, flag, flag1, entityModel, renderer);

			IRenderTypeBuffer bufferIn = event.getBuffers();

			if (rendertype != null) {
				IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
				int i = getPackedOverlay(entity, getOverlayProgress(entity, partialTicks));
				entityModel.render(matrixStack, ivertexbuilder, event.getLight(), i, 1.0F, 1.0F, 1.0F,
						flag1 ? 0.15F : 1.0F);
			}

			/**
			 * if (!entity.isSpectator()) { for(LayerRenderer<T, M> layerrenderer :
			 * this.layerRenderers) { layerrenderer.render(matrixStack, bufferIn,
			 * packedLightIn, entity, f5, f8, partialTicks, f7, f2, f6); }
			 **/

			matrixStack.pop();			

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}

	public static void renderPost(RenderLivingEvent.Post event) {
		//MatrixStack matrixStack = event.getMatrixStack();
		//matrixStack.pop();
	}

	/**
	 * Copy of method from {@linkplain LivingRenderer}.
	 * 
	 * @param entity
	 * @param partialTicks
	 * @return
	 */
	static float handleRotationFloat(LivingEntity entity, float partialTicks) {
		return (float) entity.ticksExisted + partialTicks;
	}

	static void applyRotations(LivingEntity entityLiving, MatrixStack matrixStack, float ageInTicks, float rotationYaw,
			float partialTicks) {
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
	}

	static boolean isVisible(LivingEntity entity) {
		return !entity.isInvisible();
	}

	static float getOverlayProgress(LivingEntity livingEntityIn, float partialTicks) {
		return 0.0F;
	}

	/**
	 * Resolve render type.
	 * 
	 * Copy of method func_230042_a_ from {@linkplain LivingRenderer}.
	 * 
	 * @param entity
	 * @param isVisible
	 * @param shouldResolve
	 * @param entityModel
	 * @param renderer
	 * 
	 * @return render type.
	 */
	@Nullable
	static RenderType getRenderType(LivingEntity entity, boolean isVisible, boolean shouldResolve,
			EntityModel<LivingEntity> entityModel, LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
		ResourceLocation resourcelocation = renderer.getEntityTexture(entity);
		if (shouldResolve) {
			return RenderType.entityTranslucent(resourcelocation);
		} else if (isVisible) {
			return entityModel.getRenderType(resourcelocation);
		} else {
			return entity.isGlowing() ? RenderType.outline(resourcelocation) : null;
		}
	}

	static void prepareFlatRender(MatrixStack matrixStack, double x, double z, float f) {
		// angle from positive z-axis 
		// https://gamedev.stackexchange.com/questions/14602/what-are-atan-and-atan2-used-for-in-games
		double zxAngle = Math.atan2(z, x); 
		
		// conversion to radians.
		double angle1 = zxAngle / 3.141592653589793D * 180.0D;
		
		// rotation back
		double angle2 = Math.floor((f - angle1) / 45.0D) * 45.0D;
		matrixStack.rotate(Vector3f.YP.rotationDegrees((float) angle1));
		matrixStack.scale(0.02F, 1.0F, 1.0F);
		matrixStack.rotate(Vector3f.YP.rotationDegrees((float) angle2));
	}
}
