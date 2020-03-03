package bassebombecraft.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.BUILD_MINE_BOOK;
import static bassebombecraft.ModConstants.HUD_ITEM;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemHeldInEitherHands;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.DefaultRenderingInfo.getInstance;

import bassebombecraft.rendering.DefaultBuildMineRenderer;
import bassebombecraft.rendering.DefaultCharmedRenderer;
import bassebombecraft.rendering.DefaultLookAtBlockRenderer;
import bassebombecraft.rendering.DefaultTeamRenderer;
import bassebombecraft.rendering.EntityRenderer;
import bassebombecraft.rendering.RenderingInfo;
import bassebombecraft.rendering.RenderingUtils;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for additional rendering.
 */
@Mod.EventBusSubscriber
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

	/**
	 * Renderer for rendering the block looked at by the player.
	 */
	static final EntityRenderer lookedAtBlockRenderer = new DefaultLookAtBlockRenderer();

	/**
	 * Renderer for rendering construction of the mine in the build mine book.
	 */
	static final EntityRenderer buildMineRenderer = new DefaultBuildMineRenderer();

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleEntityViewRenderEvent(EntityViewRenderEvent event) {
		// TODO: Can this replace loops for rendering of charmed, team and targets?
		return;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderLivingEvent(RenderLivingEvent event) {
		// TODO: Can this replace loops for rendering of charmed, team and targets?
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderGameOverlayEvent(RenderGameOverlayEvent event) {

		// exit if player is undefined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		// exit if targeting overlay isn't in hotbar
		if (!isItemInHotbar(player, HUD_ITEM))
			return;

		/**
		 * // get player position Vec3d playerPos = CalculatePlayerPosition(player,
		 * event.getPartialTicks());
		 * 
		 * // get font renderer Minecraft mc = Minecraft.getInstance(); FontRenderer
		 * fontRenderer = mc.fontRenderer;
		 * 
		 * String text = "Player view info:"; MainWindow window = event.getWindow(); int
		 * x = (window.getScaledWidth() - fontRenderer.getStringWidth(text)) / 2; int y
		 * = window.getScaledHeight() - TEXT_RANDOM_Y_OFFSET - 50;
		 * 
		 * fontRenderer.drawString(text, x, y, TEXT_COLOR);
		 * fontRenderer.drawString("Position: " + playerPos, x, y + 10, TEXT_COLOR);
		 * fontRenderer.drawString("Look: " + player.getLookVec(), x, y + 20,
		 * TEXT_COLOR);
		 **/
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {

		// exit if player is undefined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		// render DEBUG billboard
		// RenderingInfo info = getInstance(event.getPartialTicks());
		// DebugTextBillBoardRenderer debugRenderer = new DebugTextBillBoardRenderer();
		// debugRenderer.render(player, info);

		// render if HUD item is in hotbar
		if (isItemInHotbar(player, HUD_ITEM))
			renderHudItem(event, player);

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleHighlightBlock(HighlightBlock event) {

		// exit if player is undefined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		try {

			// create rendering info
			RenderingInfo info = getInstance(event.getPartialTicks(), event.getTarget());

			// render if HUD item is in hotbar
			if (isItemInHotbar(player, HUD_ITEM)) {
				lookedAtBlockRenderer.render(player, info);
			}

			// render if build mine book is in hand
			if (isItemHeldInEitherHands(player, BUILD_MINE_BOOK)) {
				buildMineRenderer.render(player, info);
			}

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
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
			//teamRenderer.render(player, info);
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
