package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.DECREASE_SIZE_EFFECT;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.potion.effect.DecreaseSizeEffect;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Renderer for rendering entities with size decreased by the
 * {@linkplain DecreaseSizeEffect} potion effects.
 */
public class DecreasedSizedEffectRenderer {

	/**
	 * Decreased size scale.
	 */
	static final float SCALE = 0.5f;

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(DECREASE_SIZE_EFFECT))
			return;
		
		// get and push matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();
		matrixStack.scale(SCALE, SCALE, SCALE);

		// set entity bounding box to size
		AxisAlignedBB aabb = entity.getBoundingBox().shrink(SCALE);
		entity.setBoundingBox(aabb);
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(DECREASE_SIZE_EFFECT))
			return;

		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.pop();
	}

}
