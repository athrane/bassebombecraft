package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.HUD_ITEM;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.RenderingUtils.renderBillboardText;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.item.basic.HudItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering charmed information in the {@linkplain HudItem}.
 */
public class HudItemCharmedInfoRenderer {

	/**
	 * HUD text x position.
	 */
	static final int TEXT_X_POS = 110;

	/**
	 * HUD text y position.
	 */
	static final int TEXT_Y_POS = 10;

	/**
	 * Handle {@linkplain RenderWorldLastEvent}.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD_ITEM))
				return;

			render(event.getMatrixStack(), player);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render charmed info.
	 * 
	 * @param matrixStack matrix static for rendering transforms.
	 * @param player      player object.
	 */
	static void render(MatrixStack matrixStack, PlayerEntity player) {

		// get charmed entities
		CharmedMobsRepository repository = getProxy().getClientCharmedMobsRepository();
		Stream<CharmedMob> charmedMobs = repository.get();
		int charmedSize = repository.size();

		// get render buffer
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

		// render basic info
		renderBillboardText(matrixStack, buffer, TEXT_X_POS, TEXT_Y_POS, "CHARMED MOBS");
		renderBillboardText(matrixStack, buffer, TEXT_X_POS, TEXT_Y_POS + 10, "Number chamred: " + charmedSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		charmedMobs.forEach(c -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			LivingEntity charmedEntity = c.getEntity();
			String memberName = charmedEntity.getName().getUnformattedComponentText();
			int duration = c.getDuration();
			String text = "Mob: " + memberName + ", Charm duration: " + duration;
			renderBillboardText(matrixStack, buffer, TEXT_X_POS, TEXT_Y_POS + 20 + (counter * 10), text);
		});

	}

}
