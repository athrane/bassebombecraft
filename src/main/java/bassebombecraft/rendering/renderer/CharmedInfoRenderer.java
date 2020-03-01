package bassebombecraft.rendering.renderer;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.HUD_ITEM;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.renderBillboardText;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

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
	 * Render charmed info.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void render(RenderWorldLastEvent event) {

		// exit if player is undefined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		// exit if HUD item isn't in hotbar
		if (!isItemInHotbar(player, HUD_ITEM))
			return;

		// get charmed entities
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		Collection<CharmedMob> charmedMobs = repository.get();
		int charmedSize = charmedMobs.size();

		// get render buffer and matrix stack
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		MatrixStack matrixStack = event.getMatrixStack();

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
