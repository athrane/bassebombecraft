package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.ICON_BILLBOARD_ROTATION;
import static bassebombecraft.ModConstants.HUD_TEXT_DISP;
import static bassebombecraft.client.rendering.RenderingUtils.renderTriangleBillboard;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.Collection;

import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Renderer} for rendering team entities in
 * the HUD item.
 */
@Deprecated
public class DefaultTeamRenderer implements EntityRenderer {

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "Team";

	/**
	 * Renderer for rendering target of an entity in the HUD Item.
	 */
	static final EntityRenderer targetRenderer = new DefaultTargetEntityRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// typecast
		Player player = (Player) entity;

		// get player position
		Vec3 playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// get team members
		TeamRepository repository = getProxy().getServerTeamRepository();
		Collection<LivingEntity> entities = repository.get(player);

		// loop over team members
		synchronized (entities) {
			for (LivingEntity teamEntity : entities)
				renderTeamEntity(teamEntity, playerPos, info);
		}
	}

	/**
	 * Render team member.
	 * 
	 * @param entity    team member entity
	 * @param playerPos player position
	 * @param info      rendering info.
	 */
	void renderTeamEntity(LivingEntity entity, Vec3 playerPos, RenderingInfo info) {

		Vec3 entityPos = entity.getBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, ICON_BILLBOARD_ROTATION);
		// renderTextBillboard(playerPos, entityPos, TEAM_LABEL,
		// TEXT_BILLBOARD_ROTATION);
		entityPos = entityPos.add(0, -HUD_TEXT_DISP, 0);
		// renderTextBillboard(playerPos, entityPos, getFirstRunningAiGoalName(entity),
		// TEXT_BILLBOARD_ROTATION);
		entityPos = entityPos.add(0, -HUD_TEXT_DISP, 0);
		// renderTextBillboard(playerPos, entityPos,
		// getFirstRunningAiTargetGoalName(entity), TEXT_BILLBOARD_ROTATION);
		targetRenderer.render(entity, info);
	}

}
