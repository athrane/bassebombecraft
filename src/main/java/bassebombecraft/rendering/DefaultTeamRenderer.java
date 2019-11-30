package bassebombecraft.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTriangleBillboard;

import java.util.Collection;

import javax.vecmath.Vector4f;

import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering targeted entities
 * in the HUD item.
 */
public class DefaultTeamRenderer implements EntityRenderer {

	/**
	 * Angle for rotation of billboard for triangle for rendering team members and
	 * charmed entities.
	 */
	static final int TEAM_N_CHARMED_BILLBOARD_ANGLE = 0;

	/**
	 * Rotation of billboard for triangle for rendering team members and charmed
	 * entities.
	 */
	static final Vector4f TEAM_N_CHARMED_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F,
			TEAM_N_CHARMED_BILLBOARD_ANGLE);

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "Team";

	/**
	 * Renderer for rendering bounding box of an entity in the HUD Item.
	 */
	final static EntityRenderer boundingBoxRenderer = new DefaultBoundingBoxEntityRenderer();

	/**
	 * Renderer for rendering target of an entity in the HUD Item.
	 */
	final static EntityRenderer targetRenderer = new DefaultTargetEntityRenderer();
	
	
	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// typecast
		PlayerEntity player = (PlayerEntity) entity;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// loop
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		Collection<LivingEntity> entities = repository.get(player);
		synchronized (entities) {
			for (LivingEntity teamEntity : entities)
				renderTeamEntity(teamEntity, playerPos, info);
		}
	}

	void renderTeamEntity(LivingEntity entity, Vec3d playerPos, RenderingInfo info) {
		Vec3d entityPos = entity.getBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, TEAM_N_CHARMED_BILLBOARD_ROTATION);
		renderTextBillboard(playerPos, entityPos, TEAM_LABEL, TEXT_BILLBOARD_ROTATION);
		//boundingBoxRenderer.render(entity, info);
		targetRenderer.render(entity, info);
	}

}
