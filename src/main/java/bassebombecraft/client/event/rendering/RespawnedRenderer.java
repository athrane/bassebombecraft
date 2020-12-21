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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Client side renderer for rendering entities with the respawned entity
 * attribute.
 */
public class RespawnedRenderer {

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
		boolean isLocked = lock.isLocked();
		if (isLocked)
			return;

		try {

			// acquire lock
			lock.lock();

			doRenderPre(event);

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
	 * Render pre event.
	 * 
	 * @param event rendering event
	 */
	static void doRenderPre(Pre<? super LivingEntity, ?> event) {
		LivingEntity entity = event.getEntity();

		// get oscillating value
		float oscValue = (float) GeometryUtils.oscillate(0, 1);
		int oscInt = (int) (oscValue * 255);
		int color = encodeRgbaToInt(0, oscInt, 0, 128);

		Minecraft mcClient = Minecraft.getInstance();
		OutlineLayerBuffer buffer = mcClient.getRenderTypeBuffers().getOutlineBufferSource();
		MatrixStack ms = event.getMatrixStack();
		LivingRenderer<? super LivingEntity, ?> renderer = event.getRenderer();
		float partialTicks = event.getPartialRenderTick();
		float yaw = MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);

		buffer.setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
		renderer.render(entity, yaw, partialTicks, ms, buffer, 15728640);
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

	static int encodeRgbaToInt(int red, int green, int blue, int alpha) {
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF));
	}

}
