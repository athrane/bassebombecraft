package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.ICON_BILLBOARD_ROTATION;
import static bassebombecraft.ClientModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.client.rendering.RenderingUtils.renderTextBillboard;
import static bassebombecraft.client.rendering.RenderingUtils.renderTriangleBillboard;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.stream.Stream;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering targeted entities
 * in the HUD item.
 */
@Deprecated
public class DefaultCharmedRenderer implements EntityRenderer {

	/**
	 * Charmed label.
	 */
	static final String CHARMED_LABEL = "Charmed";

	/**
	 * Renderer for rendering bounding box of an entity in the HUD Item.
	 */
	static final EntityRenderer boundingBoxRenderer = new DefaultBoundingBoxEntityRenderer();

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
		PlayerEntity player = (PlayerEntity) entity;

		// get player position
		Vector3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// get charmed entities
		CharmedMobsRepository repository = getProxy().getClientCharmedMobsRepository();
		Stream<CharmedMob> charmedMobs = repository.get();

		// loop over charmed mobs
		charmedMobs.forEach(c -> {
			renderTeamEntity(c, playerPos, info);
		});
	}

	/**
	 * Render charmed mob.
	 * 
	 * @param entity    charmed mob.
	 * @param playerPos player position
	 * @param info      rendering info.
	 */
	void renderTeamEntity(CharmedMob charmedMob, Vector3d playerPos, RenderingInfo info) {
		LivingEntity entity = charmedMob.getEntity();
		Vector3d entityPos = entity.getBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, ICON_BILLBOARD_ROTATION);
		renderTextBillboard(playerPos, entityPos, CHARMED_LABEL, TEXT_BILLBOARD_ROTATION);
		boundingBoxRenderer.render(entity, info);
		targetRenderer.render(entity, info);
	}

}
