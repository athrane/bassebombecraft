package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.RenderingUtils.renderBillboardText;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.vertex.PoseStack;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering team information in the HUD item.
 */
public class TeamInfoRenderer {

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
			Player player = getClientSidePlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD.get()))
				return;

			render(event.getMatrixStack(), player);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render team info.
	 * 
	 * @param matrixStack matrix static for rendering transforms.
	 * @param player      player object.
	 */
	static void render(PoseStack matrixStack, Player player) {

		// get team
		TeamRepository repository = getProxy().getServerTeamRepository();
		Collection<LivingEntity> team = repository.get(player);
		int teamSize = repository.size(player);

		// get current commander command
		MobCommanderRepository commanderRepository = getProxy().getServerMobCommanderRepository();
		MobCommand command = commanderRepository.getCommand(player);

		// get render buffer
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

		// render basic info
		renderBillboardText(matrixStack, buffer, -210, -110, "TEAM");
		renderBillboardText(matrixStack, buffer, -210, -100, "Commander command: " + command.getTitle());
		renderBillboardText(matrixStack, buffer, -210, -90, "Team size: " + teamSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		team.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			String memberName = m.getName().getContents();
			String targetName = getTargetName(m);
			String text = "Member: " + memberName + ", Target: " + targetName;
			renderBillboardText(matrixStack, buffer, -210, -80 + (counter * 10), text);
		});

	}

	/**
	 * Get target name.
	 * 
	 * @param entity entity to resolved target name from.
	 * 
	 * @return target name.
	 */
	static String getTargetName(LivingEntity entity) {

		// exit if entity has no target
		if (!EntityUtils.hasAliveTarget(entity))
			return "N/A";

		// get live target info
		LivingEntity target = getTarget(entity);
		return target.getName().getContents();
	}

}
