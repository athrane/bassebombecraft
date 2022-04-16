package bassebombecraft.client.rendering;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Interface for rendering stuff.
 */
@Deprecated
public interface Renderer {

	/**
	 * Render items.
	 * 
	 * @param player
	 * @param playerPos
	 */
	public void render(Player player, Vec3 playerPos);
}
