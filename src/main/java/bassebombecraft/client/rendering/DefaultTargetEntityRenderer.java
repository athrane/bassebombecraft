package bassebombecraft.client.rendering;

import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.rendering.RenderingUtils.completeSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.prepareSimpleRendering;
import static bassebombecraft.client.rendering.RenderingUtils.renderLine;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import com.mojang.blaze3d.platform.GlStateManager;

import bassebombecraft.entity.EntityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Renderer} for rendering the target of
 * entity in the HUD item.
 * 
 * The render view entity is translated along the Y-axis to adjust for the
 * player eye height.
 */
public class DefaultTargetEntityRenderer implements EntityRenderer {

	/**
	 * Renderer for rendering bounding box of an entity in the HUD Item.
	 */
	static final EntityRenderer boundingBoxRenderer = new DefaultBoundingBoxEntityRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity has no target
		if (!EntityUtils.hasAliveTarget(entity))
			return;
		LivingEntity target = getTarget(entity);

		// render bounding box
		boundingBoxRenderer.render(target, info);

		// get player
		Player player = getClientSidePlayer();

		// get player position
		Vec3 playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// render target billboards
		Vec3 targetPos = target.getBoundingBox().getCenter();
		// renderRectangleBillboard(playerPos, targetPos);
		// renderTextBillboard(playerPos, targetPos, TARGET_LABEL,
		// TEXT_BILLBOARD_ROTATION);

		// render targeting line
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();
		Vec3 start = entity.getBoundingBox().getCenter();
		Vec3 end = target.getBoundingBox().getCenter();

		prepareSimpleRendering(x, y, z);
		//GlStateManager._color4f(1F, 1F, 1F, 1F);
		renderLine(start, end);
		completeSimpleRendering();
	}

}
