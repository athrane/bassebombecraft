package bassebombecraft.client.event.rendering;

import static bassebombecraft.ModConstants.INCREASE_SIZE_EFFECT;
import static bassebombecraft.client.rendering.RenderingUtils.oscillate;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.potion.effect.IncreaseSizeEffect;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with size increased by the
 * {@linkplain IncreaseSizeEffect} potion effect.
 */
public class IncreaseSizeEffectRenderer {

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(INCREASE_SIZE_EFFECT))
			return;

		// get calculated size
		EffectInstance effectInstance = entity.getActivePotionEffect(INCREASE_SIZE_EFFECT);
		float scale = calculateSize(effectInstance.getAmplifier(), entity);

		// get and push matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();
		matrixStack.scale(scale, scale, scale);

		// set entity bounding box to size
		AxisAlignedBB aabb = entity.getBoundingBox().shrink(scale);
		entity.setBoundingBox(aabb);
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(INCREASE_SIZE_EFFECT))
			return;

		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.pop();
	}

	/**
	 * Calculate increased entry size.
	 * 
	 * @param amplifier potion effect amplifier.
	 * @param entity    entity to calculate size for.
	 * 
	 * @return increased entry size.
	 */
	public static float calculateSize(float amplifier, LivingEntity entity) {
		float scaledSize = (float) amplifier / 100.0F;
		float scaledSizeFraction = scaledSize * 0.25F;
		float sizeVariation = (float) oscillate(entity.hashCode(), 0, scaledSizeFraction);
		return scaledSize + sizeVariation;
	}

}
