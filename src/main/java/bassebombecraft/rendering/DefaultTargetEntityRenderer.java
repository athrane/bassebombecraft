package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.TARGET_LABEL;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.rendering.RenderingUtils.completeSimpleRendering;
import static bassebombecraft.rendering.RenderingUtils.prepareSimpleRendering;
import static bassebombecraft.rendering.RenderingUtils.renderLine;
import static bassebombecraft.rendering.RenderingUtils.renderRectangleBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;

import com.mojang.blaze3d.platform.GlStateManager;

import bassebombecraft.entity.EntityUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

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
	final static EntityRenderer boundingBoxRenderer = new DefaultBoundingBoxEntityRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity has no target
		if (!EntityUtils.hasAliveTarget(entity))
			return;
		LivingEntity target = getTarget(entity);

		// render bounding box
		//boundingBoxRenderer.render(target, info);

		// get player
		PlayerEntity player = getPlayer();

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// render target billboards
		Vec3d targetPos = target.getBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);

		// render targeting line
		double x = info.getRveTranslatedViewX();
		double y = info.getRveTranslatedViewYOffsetWithPlayerEyeHeight();
		double z = info.getRveTranslatedViewZ();
		Vec3d start = entity.getBoundingBox().getCenter();
		Vec3d end = target.getBoundingBox().getCenter();

		prepareSimpleRendering(x, y, z);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		renderLine(start, end);
		completeSimpleRendering();
	}

}