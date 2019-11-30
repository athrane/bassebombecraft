package bassebombecraft.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.TARGET_LABEL;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.rendering.RenderingUtils.renderLineBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderRectangleBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;

import java.util.stream.Stream;

import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering targeted entities
 * in the HUD item.
 */
public class DefaultTargetsRenderer implements Renderer {

	@Override
	public void render(PlayerEntity player, Vec3d playerPos) {
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();

		// get entities
		Stream<LivingEntity> members = repository.get(player);
		members.forEach(e -> renderTargetedEntity(e, playerPos));
	}

	void renderTargetedEntity(LivingEntity target, Vec3d playerPos) {
		Vec3d targetPos = target.getBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
	}

	void renderTargetedEntity(LivingEntity entity, LivingEntity target, Vec3d playerPos) {
		Vec3d entityPos = entity.getBoundingBox().getCenter();
		Vec3d targetPos = target.getBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
		renderLineBillboard(playerPos, entityPos, targetPos);
	}

}
