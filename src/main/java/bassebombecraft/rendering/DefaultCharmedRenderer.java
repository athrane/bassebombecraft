package bassebombecraft.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTriangleBillboard;

import java.util.Collection;

import javax.vecmath.Vector4f;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering targeted entities
 * in the HUD item.
 */
public class DefaultCharmedRenderer implements EntityRenderer {

	/**
	 * Angle for rotation of billboard for triangle for rendering charmed entities.
	 */
	static final int BILLBOARD_ANGLE = 0;

	/**
	 * Rotation of billboard for triangle for rendering charmed entities.
	 */
	static final Vector4f BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, BILLBOARD_ANGLE);

	/**
	 * Charmed label.
	 */
	static final String CHARMED_LABEL = "Charmed";

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

		// get charmed entities
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		Collection<CharmedMob> entities = repository.get();

		// loop over charmed mob
		synchronized (entities) {
			for (CharmedMob charmedMob : entities)
				renderTeamEntity(charmedMob, playerPos, info);
		}
	}

	/**
	 * Render charmed mob.
	 * 
	 * @param entity    charmed mob.
	 * @param playerPos player position
	 * @param info      rendering info.
	 */
	void renderTeamEntity(CharmedMob charmedMob, Vec3d playerPos, RenderingInfo info) {
		LivingEntity entity = charmedMob.getEntity();
		Vec3d entityPos = entity.getBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, BILLBOARD_ROTATION);
		renderTextBillboard(playerPos, entityPos, CHARMED_LABEL, TEXT_BILLBOARD_ROTATION);
		boundingBoxRenderer.render(entity, info);
		targetRenderer.render(entity, info);
	}

}
