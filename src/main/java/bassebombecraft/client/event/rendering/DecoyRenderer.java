package bassebombecraft.client.event.rendering;

import static bassebombecraft.entity.EntityUtils.isEntityAttributeSet;
import static bassebombecraft.entity.attribute.RegisteredAttributes.IS_DECOY_ATTRIBUTE;
import static bassebombecraft.geom.GeometryUtils.oscillateWithDeltaTime;
import static net.minecraft.util.Mth.rotLerp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
	public static void handleRenderLivingEventPre(Pre<Player, PlayerModel<Player>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!isEntityAttributeSet(entity, IS_DECOY_ATTRIBUTE.get()))
			return;

		// get calculated size
		float scale = calculateSize(DECOY_SCALE, entity);

		// get and push matrix stack
		PoseStack matrixStack = event.getMatrixStack();
		matrixStack.pushPose();

		// calculate xz angle from positive z-axis
		// https://gamedev.stackexchange.com/questions/14602/what-are-atan-and-atan2-used-for-in-games
		float partialTicks = event.getPartialRenderTick();
		float f = rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
		double x = entity.getX();
		double z = entity.getZ();
		double zxAngle = Math.atan2(z, x);

		// convert angle to radians
		double angle1 = zxAngle / Math.PI * 180.0D;

		// calculate rotation back
		double angle2 = Math.floor((f - angle1) / 45.0D) * 45.0D;

		// rotate xz, scale depth to "0", rotate back
		matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) angle1));
		matrixStack.scale(THIN_DEPTH, scale, scale);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) angle2));
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!isEntityAttributeSet(entity, IS_DECOY_ATTRIBUTE.get()))
			return;

		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
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
