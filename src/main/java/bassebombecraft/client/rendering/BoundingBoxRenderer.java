package bassebombecraft.client.rendering;

import net.minecraft.world.phys.AABB;

/**
 * Interface for rendering a single bounding box.
 */
@Deprecated
public interface BoundingBoxRenderer {

	/**
	 * Render entity.
	 * 
	 * @param aabb bounding box to render something for.
	 * @param info rendering info.
	 */
	public void render(AABB aabb, RenderingInfo info);
}
