package bassebombecraft.client.rendering;

import net.minecraft.world.entity.LivingEntity;

/**
 * Interface for rendering a single entity.
 */
@Deprecated
public interface EntityRenderer {

	/**
	 * Render entity.
	 * 
	 * @param entity entity to render something for.
	 * @param info   rendering info.
	 */
	public void render(LivingEntity entity, RenderingInfo info);
}
