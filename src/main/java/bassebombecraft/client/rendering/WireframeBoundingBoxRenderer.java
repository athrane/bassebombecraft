package bassebombecraft.client.rendering;

import static bassebombecraft.client.rendering.RenderingUtils.renderWireframeBox;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.world.phys.AABB;

/**
 * Implementation of the {@linkplain Renderer} for rendering bounding box in the
 * HUD item as a wire frame.
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class WireframeBoundingBoxRenderer implements BoundingBoxRenderer {

	@Override
	public void render(AABB aabb, RenderingInfo info) {
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();

		// prepareSimpleRendering(x, y, z);
		//GlStateManager._color4f(1F, 1F, 1F, 1F);
		renderWireframeBox(aabb);
		// completeSimpleRendering();
	}

}
