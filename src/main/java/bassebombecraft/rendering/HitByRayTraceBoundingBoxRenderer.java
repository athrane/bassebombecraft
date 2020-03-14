package bassebombecraft.rendering;

import static bassebombecraft.rendering.RenderingUtils.completeSimpleRendering;
import static bassebombecraft.rendering.RenderingUtils.oscillate;
import static bassebombecraft.rendering.RenderingUtils.prepareSimpleRendering;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxBottom;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxEast;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxNorth;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxSouth;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxTop;
import static bassebombecraft.rendering.RenderingUtils.renderSolidBoxWest;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain BoundingBoxRenderer} for rendering the side
 * of a bounding bounding box hit by ray trace (result).
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class HitByRayTraceBoundingBoxRenderer implements BoundingBoxRenderer {

	@Override
	public void render(AxisAlignedBB aabb, RenderingInfo info) {
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();

		// exit if ray trace result isn't defined
		if (!info.isRayTraceResultDefined())
			return;

		// exit if ray trace result isn't defined
		if (!info.isRayTraceResultDefined())
			return;

		// get ray trace result
		RayTraceResult result = info.getResult();

		// type cast
		BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

		// get direction
		Direction direction = blockResult.getFace();

		// grow box to avoid artifacts
		aabb = aabb.grow(0.01);

		prepareSimpleRendering(x, y, z);

		GlStateManager.enableBlend();
		//GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture();
		GlStateManager.depthMask(false);

		// oscillate alpha
		float alpha = (float) oscillate(0.25F, 1.0F);
		GlStateManager.color4f(0.75F, 0.75F, 0, alpha);

		switch (direction) {
		case UP:
			renderSolidBoxTop(aabb);
			break;

		case DOWN:
			renderSolidBoxBottom(aabb);
			break;

		case EAST:
			renderSolidBoxEast(aabb);
			break;

		case NORTH:
			renderSolidBoxNorth(aabb);
			break;

		case SOUTH:
			renderSolidBoxSouth(aabb);
			break;

		case WEST:
			renderSolidBoxWest(aabb);

		default: // NO-OP
		}

		GlStateManager.depthMask(true);
		completeSimpleRendering();
	}
}
