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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering charmed information in the HUD item.
 */
public class CharmedInfoRenderer {

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
		CharmedMobsRepository repository = getProxy().getCharmedMobsRepository(player.getEntityWorld());
		Stream<CharmedMob> charmedMobs = repository.get();
		int charmedSize = repository.size();

		// get render buffer
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

		// render basic info
		renderBillboardText(matrixStack, buffer, 120, 10, "CHARMED MOBS");
		renderBillboardText(matrixStack, buffer, 120, 20, "Number chamred: " + charmedSize);

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
			renderBillboardText(matrixStack, buffer, 120, 30 + (counter * 10), text);
		});

	}

}
