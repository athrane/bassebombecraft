package bassebombecraft.client.rendering;

import static bassebombecraft.client.rendering.RenderingUtils.completeSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.prepareSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBox;
import static bassebombecraft.geom.GeometryUtils.oscillate;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.world.phys.AABB;

/**
 * Implementation of the {@linkplain Renderer} for rendering bounding box a
 * solid box.
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class SolidBoundingBoxRenderer implements BoundingBoxRenderer {

	@Override
	public void render(AABB aabb, RenderingInfo info) {
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();

		// grow box to avoid artifacts
		aabb = aabb.inflate(0.01);

		prepareSimpleRendering(x, y, z);

		GlStateManager._enableBlend();
		// GlStateManager.blendFunc(SourceFactor.SRC_ALPHA,
		// DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager._disableTexture();
		GlStateManager._depthMask(false);

		float alpha = (float) oscillate(0.25F, 1.0F);
		GlStateManager._color4f(0.75F, 0.75F, 0, alpha);
		renderSolidBox(aabb);

		GlStateManager._depthMask(true);
		completeSimpleRendering();
	}
}
