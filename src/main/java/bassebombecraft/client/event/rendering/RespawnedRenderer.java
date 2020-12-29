package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.IS_RESPAWNED;
import static bassebombecraft.entity.EntityUtils.hasAttribute;

import java.util.concurrent.locks.ReentrantLock;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.geom.GeometryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with the respawned entity
 * attribute.
 * 
 * A lock is used to ensure is only rendered once. This is due to invocation of
 * the render method spawns a new {@linkplain RenderLivingEvent} event which
 * lead to invocation of this handler once more.
 */
public class RespawnedRenderer {

	/**
	 * Outline color, red component.
	 */
	static final int RGB_RED = 128;
	
	/**
	 * Outline color, green component.
	 */	
	static final int RGB_GREEN = 192;
	
	/**
	 * Outline color, blue component.
	 */	
	static final int RGB_BLUE = 128;
	
	/**
	 * Outline color, alpha offset.
	 */	
	static final int ALPHA_OFFSET = 127;
	
	/**
	 * Lock used to avoid stack over flow of {@linkplain RenderLivingEvent} events.
	 */
	static ReentrantLock lock = new ReentrantLock();

	/**
	 * Handle {@linkplain RenderLivingEvent.Pre} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPre(Pre<? super LivingEntity, ?> event) {

		// exit if entity attribute isn't defined
		if (!hasAttribute(event.getEntity(), IS_RESPAWNED))
			return;

		// exit if lock is already locked
		if (lock.isLocked())
			return;

		try {

			// acquire lock and render
			lock.lock();
			doRender(event);

			// cancel event - i.e. main rendering of entity
			event.setCanceled(true);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {

			// unlock despite exception to avoid lock deadlocks
			lock.unlock();
		}
	}

	/**
	 * Render entity.
	 * 
	 * Renders with the event renderer {@linkplain LivingRenderer}.
	 * 
	 * Uses the {@linkplain OutlineLayerBuffer} to set colors.
	 * 
	 * @param event rendering event
	 */
	static void doRender(Pre<? super LivingEntity, ?> event) {
		LivingEntity entity = event.getEntity();

		// get oscillating value
		float oscValue = (float) GeometryUtils.oscillate(0, 1);
		int alpha = (int) (oscValue * 128);

		// get outline buffer and set color
		Minecraft mcClient = Minecraft.getInstance();
		OutlineLayerBuffer buffer = mcClient.getRenderTypeBuffers().getOutlineBufferSource();
		buffer.setColor(RGB_RED, RGB_GREEN, RGB_BLUE, ALPHA_OFFSET + alpha);

		// get matrix stack
		MatrixStack matrixStack = event.getMatrixStack();

		// calculate yaw
		float partialTicks = event.getPartialRenderTick();
		float yaw = entity.getYaw(partialTicks);

		// get light
		int packedLight = event.getLight();

		// render
		event.getRenderer().render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);

		// should be maintained for outline buffer.
		buffer.finish();
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<?, PlayerModel<?>> event) {
		LivingEntity entity = event.getEntity();

		// exit if entity attribute isn't defined
		if (!hasAttribute(entity, IS_RESPAWNED))
			return;

		// NO-OP
	}

}
