package bassebombecraft.client.event.rendering;

import static bassebombecraft.geom.GeometryUtils.oscillateWithDeltaTime;
import static bassebombecraft.potion.effect.RegisteredEffects.DECREASE_SIZE_EFFECT;

import com.mojang.blaze3d.vertex.PoseStack;

import bassebombecraft.potion.effect.DecreaseSizeEffect;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with size decreased by the
 * {@linkplain DecreaseSizeEffect} potion effect.
 */
public class DecreaseSizeEffectRenderer {

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<Player, PlayerModel<Player>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.hasEffect(DECREASE_SIZE_EFFECT.get()))
			return;

		// get calculated size
		MobEffectInstance effectInstance = entity.getEffect(DECREASE_SIZE_EFFECT.get());
		float scale = calculateSize(effectInstance.getAmplifier(), entity);

		// get and push matrix stack
		PoseStack matrixStack = event.getMatrixStack();
		matrixStack.pushPose();
		matrixStack.scale(scale, scale, scale);

		// set entity bounding box to size
		AABB aabb = entity.getBoundingBox().deflate(scale);
		entity.setBoundingBox(aabb);
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<Player, PlayerModel<Player>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.hasEffect(DECREASE_SIZE_EFFECT.get()))
			return;

		// get and pop matrix stack
		PoseStack matrixStack = event.getMatrixStack();
		matrixStack.popPose();
	}

	/**
	 * Calculate decreased entry size.
	 * 
	 * @param amplifier potion effect amplifier.
	 * @param entity    entity to calculate size for.
	 * 
	 * @return decreased entry size.
	 */
	public static float calculateSize(float amplifier, LivingEntity entity) {
		float scaledSize = (float) amplifier / 100.0F;
		float scaledSizeFraction = scaledSize * 0.25F;
		float sizeVariation = (float) oscillateWithDeltaTime(entity.hashCode(), 0, scaledSizeFraction);
		return scaledSize + sizeVariation;
	}

}
