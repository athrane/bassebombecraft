package bassebombecraft.client.rendering;

import static bassebombecraft.client.rendering.RenderingUtils.completeSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.prepareSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxBottom;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxEast;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxNorth;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxSouth;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxTop;
import static bassebombecraft.client.rendering.RenderingUtils.renderSolidBoxWest;
import static bassebombecraft.geom.GeometryUtils.oscillate;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Implementation of the {@linkplain BoundingBoxRenderer} for rendering the side
 * of a bounding bounding box hit by ray trace (result).
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class HitByRayTraceBoundingBoxRenderer implements BoundingBoxRenderer {

	@Override
	public void render(AABB aabb, RenderingInfo info) {
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
		HitResult result = info.getResult();

		// type cast
		BlockHitResult blockResult = (BlockHitResult) result;

		// get direction
		Direction direction = blockResult.getDirection();

		// grow box to avoid artifacts
		aabb = aabb.inflate(0.01);

		prepareSimpleRendering(x, y, z);

		GlStateManager._enableBlend();
		//GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager._disableTexture();
		GlStateManager._depthMask(false);

		// oscillate alpha
		float alpha = (float) oscillate(0.25F, 1.0F);
		GlStateManager._color4f(0.75F, 0.75F, 0, alpha);

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

		GlStateManager._depthMask(true);
		completeSimpleRendering();
	}
}
