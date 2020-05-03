package bassebombecraft.client.event.rendering;

import static bassebombecraft.ModConstants.DECOY;
import static bassebombecraft.client.rendering.RenderingUtils.oscillate;
import static bassebombecraft.entity.EntityUtils.hasAttribute;
import static net.minecraft.util.math.MathHelper.interpolateAngle;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with the decoy entity attribute.
 */
public class DecoyRenderer {

	/**
	 * Paper thing depth of decoy.
	 */
	static final float THIN_DEPTH = 0.02F;

	/**
	 * Decoy size in procentage.
	 */
	static final float DECOY_SCALE = 200.0F;

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!hasAttribute(entity, DECOY))
			return;

		// get calculated size
		float scale = calculateSize(DECOY_SCALE, entity);

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

		// exit if entity attribute isn't defined
		if (!hasAttribute(entity, DECOY))
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
