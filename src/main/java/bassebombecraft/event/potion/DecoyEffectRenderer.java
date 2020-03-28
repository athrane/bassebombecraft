package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.DECOY_EFFECT;
import static bassebombecraft.rendering.RenderingUtils.oscillate;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.potion.effect.DecoyEffect;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import static net.minecraft.util.math.MathHelper.*;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.fml.common.Mod;

/**
 * Client side renderer for rendering entities with the {@linkplain DecoyEffect}
 * potion effect.
 */
@Mod.EventBusSubscriber
public class DecoyEffectRenderer {

	/**
	 * Paper thing depth of decoy.s
	 */
	static final float THIN_DEPTH = 0.02F;

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(DECOY_EFFECT))
			return;

		// get calculated size
		EffectInstance effectInstance = entity.getActivePotionEffect(DECOY_EFFECT);
		float scale = calculateSize(effectInstance.getAmplifier(), entity);

		// get and push matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();

		// calculate xz angle from positive z-axis
		// https://gamedev.stackexchange.com/questions/14602/what-are-atan-and-atan2-used-for-in-games
		float partialTicks = event.getPartialRenderTick();
		float f = interpolateAngle(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
		double x = entity.getPosX();
		double z = entity.getPosZ();
		double zxAngle = Math.atan2(z, x);

		// convert angle to radians
		double angle1 = zxAngle / Math.PI * 180.0D;

		// calculate rotation back
		double angle2 = Math.floor((f - angle1) / 45.0D) * 45.0D;

		// rotate xz, scale depth to "0", rotate back
		matrixStack.rotate(Vector3f.YP.rotationDegrees((float) angle1));
		matrixStack.scale(THIN_DEPTH, scale, scale);
		matrixStack.rotate(Vector3f.YP.rotationDegrees((float) angle2));
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(DECOY_EFFECT))
			return;

		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.pop();
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
		float sizeVariation = (float) oscillate(entity.hashCode(), 0, scaledSizeFraction);
		return scaledSize + sizeVariation;
	}

}
