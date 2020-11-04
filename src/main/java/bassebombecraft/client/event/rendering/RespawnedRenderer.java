package bassebombecraft.client.event.rendering;

import static bassebombecraft.ModConstants.IS_RESPAWNED;
import static bassebombecraft.entity.EntityUtils.hasAttribute;

import com.mojang.blaze3d.matrix.MatrixStack;
import static com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA;
import static com.mojang.blaze3d.platform.GlStateManager.DestFactor.*;

import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.geom.GeometryUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with the respawned entity
 * attribute.
 */
public class RespawnedRenderer {

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!hasAttribute(entity, IS_RESPAWNED))
			return;

		// get and push matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();
		
		// do ....
		float oscValue = (float) GeometryUtils.oscillate(0, 1);
		RenderSystem.blendFunc(SRC_ALPHA, ONE);
		RenderSystem.color4f(oscValue, oscValue, oscValue, 0.5f);		
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!hasAttribute(entity, IS_RESPAWNED))
			return;


		// do ....
		RenderSystem.blendFunc(SRC_ALPHA, ONE_MINUS_SRC_ALPHA);
		
		// get and pop matrix stack
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.pop();
	}

}
