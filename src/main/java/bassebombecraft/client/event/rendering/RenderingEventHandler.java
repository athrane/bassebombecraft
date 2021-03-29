package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.DefaultRenderingInfo.getInstance;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import bassebombecraft.client.player.ClientPlayerUtils;
import bassebombecraft.client.rendering.DefaultCharmedRenderer;
import bassebombecraft.client.rendering.DefaultTeamRenderer;
import bassebombecraft.client.rendering.EntityRenderer;
import bassebombecraft.client.rendering.RenderingInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Event handler for additional rendering.
 */
@Deprecated
public class RenderingEventHandler {

	/**
	 * Random constant to put text above the hotbar.
	 */
	static final int TEXT_RANDOM_Y_OFFSET = 27;

	/**
	 * Renderer for rendering team in the HUD Item.
	 */
	static final EntityRenderer teamRenderer = new DefaultTeamRenderer();

	/**
	 * Renderer for rendering charmed mobs in the HUD Item.
	 */
	static final EntityRenderer charmedRenderer = new DefaultCharmedRenderer();

	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {

		// exit if player is undefined
		if (!isClientSidePlayerDefined())
			return;

		// get player
		PlayerEntity player = ClientPlayerUtils.getClientSidePlayer();

		// render DEBUG billboard
		// RenderingInfo info = getInstance(event.getPartialTicks());
		// DebugTextBillBoardRenderer debugRenderer = new DebugTextBillBoardRenderer();
		// debugRenderer.render(player, info);

		// render if HUD item is in hotbar
		if (isItemInHotbar(player, HUD.get()))
			renderHudItem(event, player);

	}

	/**
	 * Render HUB item.
	 * 
	 * @param event  rendering event.
	 * @param player player object.
	 */
	static void renderHudItem(RenderWorldLastEvent event, PlayerEntity player) {

		try {
			// create rendering info
			RenderingInfo info = getInstance(event.getPartialTicks());

			// render
			// teamRenderer.render(player, info);
			charmedRenderer.render(player, info);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
