package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.DECREASE_SIZE_EFFECT;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.potion.effect.DecreaseSizeEffect;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.fml.common.Mod;

/**
 * Client side renderer for rendering entities with size decreased by the
 * {@linkplain DecreaseSizeEffect} potion effect.
 */
@Mod.EventBusSubscriber
public class DecreaseSizedEffectRenderer {

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if effect isn't active
		if (!entity.isPotionActive(DECREASE_SIZE_EFFECT))
			return;

		// get calculated size
		EffectInstance effectInstance = entity.getActivePotionEffect(DECREASE_SIZE_EFFECT);
		Effect effect = effectInstance.getPotion();
		DecreaseSizeEffect decreaseSizeEffect = (DecreaseSizeEffect) effect;
		float scale = decreaseSizeEffect.getSize();

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
		if (!entity.isPotionActive(DECREASE_SIZE_EFFECT))
			return;

		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.pop();
	}

}
