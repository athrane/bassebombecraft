package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isEntityAttributeSet;
import static bassebombecraft.entity.attribute.RegisteredAttributes.IS_RESPAWNED_ATTRIBUTE;
import static bassebombecraft.geom.GeometryUtils.oscillate;

import java.util.concurrent.locks.ReentrantLock;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
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
		if (!isEntityAttributeSet(event.getEntity(), IS_RESPAWNED_ATTRIBUTE.get()))
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
		float oscValue = (float) oscillate(0, 1);
		int alpha = (int) (oscValue * 128);

		// get outline buffer and set color
		Minecraft mcClient = Minecraft.getInstance();
		OutlineBufferSource buffer = mcClient.renderBuffers().outlineBufferSource();
		buffer.setColor(RGB_RED, RGB_GREEN, RGB_BLUE, ALPHA_OFFSET + alpha);

		// get matrix stack
		PoseStack matrixStack = event.getMatrixStack();

		// calculate yaw
		float partialTicks = event.getPartialRenderTick();
		float yaw = entity.getViewYRot(partialTicks);

		// get light
		int packedLight = event.getLight();

		// render
		event.getRenderer().render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);

		// should be maintained for outline buffer.
		buffer.endOutlineBatch();
	}

	/**
	 * Handle {@linkplain RenderLivingEvent.Post} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderLivingEventPost(Post<?, PlayerModel<?>> event) {

		// exit if entity attribute isn't defined
		if (!isEntityAttributeSet(event.getEntity(), IS_RESPAWNED_ATTRIBUTE.get()))
			return;

		// NO-OP
	}

}
