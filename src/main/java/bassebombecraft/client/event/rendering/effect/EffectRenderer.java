package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Client side renderer for rendering graphics effects.
 */
public class EffectRenderer {

	/**
	 * Handle {@linkplain RenderWorldLastEvent} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player isn't defined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// get world
			World world = player.getEntityWorld();

			// render particles
			render(world);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render effects.
	 * 
	 * @param world world object.
	 * 
	 * @throws Exception if rendering fails.
	 */
	static void render(World world) throws Exception {
	}

}
