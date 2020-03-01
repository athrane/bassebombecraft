package bassebombecraft.rendering.renderer;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.HUD_ITEM;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.renderBillboardText;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering team information in the HUD item.
 */
public class TeamInfoRenderer {

	/**
	 * Render team info.
	 * 
	 * @param event event to render infor for.
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
		
		// get team
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		Collection<LivingEntity> team = repository.get(player);
		int teamSize = repository.size(player);

		// get current commander command
		MobCommanderRepository commanderRepository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = commanderRepository.getCommand(player);

		// get render buffer and maxtrix stack
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		MatrixStack matrixStack = event.getMatrixStack();

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

			String memberName = m.getName().getUnformattedComponentText();
			String targetName = getTargetName(m);
			String text = "Member: " + memberName + ", Target: " + targetName;
			renderBillboardText(matrixStack, buffer, -200, -80 + (counter * 10), text);
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
		return target.getName().getUnformattedComponentText();
	}

}
