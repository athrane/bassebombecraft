package bassebombecraft.rendering.renderer;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.HUD_ITEM;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.renderBillboardText;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering target information in the HUD item.
 */
public class TargetInfoRenderer {

	/**
	 * Handle {@linkplain RenderWorldLastEvent}.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {
			
			// exit if player is undefined
			if (!isPlayerDefined())
				return;

			// get player
			PlayerEntity player = getPlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD_ITEM))
				return;
			
			render(event.getMatrixStack(), player);
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
	
	/**
	 * Render target info.
	 * 
	 * @param matrixStack matrix static for rendering transforms.
	 * @param player player object.
	 */
	static void render(MatrixStack matrixStack, PlayerEntity player) {

		// get targets
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		Stream<LivingEntity> targets = repository.get(player);
		int targetsSize = repository.size(player);

		// get current commander target
		String commanderTargetName = getCommanderTargetName(player);

		// get render buffer
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

		// render basic info
		renderBillboardText(matrixStack, buffer, 120, -110, "TARGETS");
		renderBillboardText(matrixStack, buffer, 120, -100, "Commander target: " + commanderTargetName);
		renderBillboardText(matrixStack, buffer, 120, -90, "Number targets: " + targetsSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		targets.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			String targetName = m.getName().getUnformattedComponentText();
			String text = "Target: " + targetName;
			renderBillboardText(matrixStack, buffer, 120, -80 + (counter * 10), text);
		});
	}

	/**
	 * Get commander target name.
	 * 
	 * @param player commander to resolved target name from.
	 * 
	 * @return commander target name.
	 */
	static String getCommanderTargetName(PlayerEntity player) {

		// get commander target
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		Optional<LivingEntity> optTarget = repository.getFirst(player);

		// exit if entity has no target
		if (!optTarget.isPresent())
			return "N/A";

		// get live target info
		LivingEntity target = optTarget.get();
		return target.getName().getUnformattedComponentText();
	}

}
