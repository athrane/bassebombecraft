package bassebombecraft.rendering;

import static bassebombecraft.rendering.RenderingUtils.renderSolidBox;

import java.time.Instant;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * Implementation of the {@linkplain Renderer} for rendering bounding box in the
 * HUD item as a solid box.
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class SolidBoundingBoxRenderer implements BoundingBoxRenderer {

	@Override
	public void render(AxisAlignedBB aabb, RenderingInfo info) {
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();

		// grow box to avoid artifacts
		aabb = aabb.grow(0.01);

		// prepareSimpleRendering(x, y, z);
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		GlStateManager.disableLighting();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture();
		GlStateManager.depthMask(false);

		// GlStateManager.depthMask(false);
		// GlStateManager.alphaFunc(GL11.GL_LESS, 1.0F);
		// GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GlStateManager.disableTexture();

		float alpha = (float) oscillate(0.25, 1.0);
		GlStateManager.color4f(0.75F, 0.75F, 0, alpha);
		renderSolidBox(aabb);

		// completeSimpleRendering();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	public static double oscillate(double min, double max) {
		long time = Instant.now().toEpochMilli() / 10;
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}

	public static double oscillate(double time, double min, double max) {
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}
}
