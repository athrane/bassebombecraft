package bassebombecraft.client.rendering;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

/**
 * Implementation of the {@linkplain Renderer} for rendering entity bounding box
 * in the HUD item.
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class DefaultBoundingBoxEntityRenderer implements EntityRenderer {

	/**
	 * Renderer for rendering a bounding box with a wire frame.
	 */
	static final BoundingBoxRenderer aabbWireframeRenderer = new WireframeBoundingBoxRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {
		AABB aabb = entity.getBoundingBox();
		aabbWireframeRenderer.render(aabb, info);
	}

}
