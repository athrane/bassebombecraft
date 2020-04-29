package bassebombecraft.client.rendering;

import net.minecraft.entity.LivingEntity;

/**
 * Interface for rendering a single entity.
 */
public interface EntityRenderer {

	/**
	 * Render entity.
	 * 
	 * @param entity entity to render something for.
	 * @param info   rendering info.
	 */
	public void render(LivingEntity entity, RenderingInfo info);
}
