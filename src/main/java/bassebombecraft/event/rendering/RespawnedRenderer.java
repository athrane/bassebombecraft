package bassebombecraft.event.rendering;

import static bassebombecraft.ModConstants.IS_RESPAWNED;
import static bassebombecraft.entity.EntityUtils.hasAttribute;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.fml.common.Mod;

/**
 * Client side renderer for rendering entities with the respawned entity
 * attribute.
 */
@Mod.EventBusSubscriber
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

		// RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// RenderSystem.enableBlend();
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		// NO-OP
	}

}
