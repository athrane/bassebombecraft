package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.stream.Stream;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
		Player player = (Player) entity;

		// get player position
		Vec3 playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

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
	void renderTeamEntity(CharmedMob charmedMob, Vec3 playerPos, RenderingInfo info) {
		LivingEntity entity = charmedMob.getEntity();
		Vec3 entityPos = entity.getBoundingBox().getCenter();
		// renderTriangleBillboard(playerPos, entityPos, ICON_BILLBOARD_ROTATION);
		// renderTextBillboard(playerPos, entityPos, CHARMED_LABEL,
		// TEXT_BILLBOARD_ROTATION);
		// boundingBoxRenderer.render(entity, info);
		// targetRenderer.render(entity, info);
	}

}
