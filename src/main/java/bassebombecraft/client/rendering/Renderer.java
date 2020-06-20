package bassebombecraft.client.rendering;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Interface for rendering stuff.
 */
public interface Renderer {

	/**
	 * Render items.
	 * 
	 * @param player
	 * @param playerPos
	 */
	public void render(PlayerEntity player, Vec3d playerPos);
}
