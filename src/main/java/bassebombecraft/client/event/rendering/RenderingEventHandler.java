package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.DefaultRenderingInfo.getInstance;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import bassebombecraft.client.player.ClientPlayerUtils;
import bassebombecraft.client.rendering.DefaultCharmedRenderer;
import bassebombecraft.client.rendering.DefaultTeamRenderer;
import bassebombecraft.client.rendering.EntityRenderer;
import bassebombecraft.client.rendering.RenderingInfo;
import bassebombecraft.client.rendering.RenderingUtils;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, event.getPartialTicks());

		try {
			// create rendering info
			RenderingInfo info = getInstance(event.getPartialTicks());

			// render
			// teamRenderer.render(player, info);
			charmedRenderer.render(player, info);

			Vec3d renderPos = RenderingUtils.getRenderPos();
			Vec3d translation = playerPos.subtract(renderPos);
			renderCompass(translation);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render compass.
	 * 
	 * @param translation translation vector.
	 */
	static void renderCompass(Vec3d translation) {
		Vec3d ct = translation.add(5, 0, 0);
		Vector4f br1 = new Vector4f(0.0F, 1.0F, 0.0F, 0.0F);
		RenderingUtils.renderRotatedTextBillboard(ct, br1, "-E-");

		Vector4f br2 = new Vector4f(0.0F, 1.0F, 0.0F, 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br2, "-NE-");

		Vector4f br3 = new Vector4f(0.0F, 1.0F, 0.0F, 90);
		RenderingUtils.renderRotatedTextBillboard(ct, br3, "-N-");

		Vector4f br4 = new Vector4f(0.0F, 1.0F, 0.0F, 90 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br4, "-NW-");

		Vector4f br5 = new Vector4f(0.0F, 1.0F, 0.0F, 180);
		RenderingUtils.renderRotatedTextBillboard(ct, br5, "-W-");

		Vector4f br6 = new Vector4f(0.0F, 1.0F, 0.0F, 180 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br6, "-SW-");

		Vector4f br7 = new Vector4f(0.0F, 1.0F, 0.0F, 270);
		RenderingUtils.renderRotatedTextBillboard(ct, br7, "-S-");

		Vector4f br8 = new Vector4f(0.0F, 1.0F, 0.0F, 270 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br8, "-SE-");
	}

}
