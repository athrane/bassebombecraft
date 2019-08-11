package bassebombecraft.rendering;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;

/**
 * Rendering utilities.
 */
public class RenderingUtils {

	/**
	 * Setup billboard rotation to face camera.
	 */
	public static void setupBillboardRotation() {
		final Minecraft mc = Minecraft.getInstance();
		EntityRendererManager renderManager = mc.getRenderManager();
		GlStateManager.rotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	}

	/**
	 * Setup billboard rotation to face camera.
	 */
	public static void setupBillboardRotationV2() {
		final Minecraft mc = Minecraft.getInstance();
		final Entity rve = mc.getRenderViewEntity();
		GlStateManager.rotatef(-rve.rotationYaw, 0, 1, 0);
		GlStateManager.rotatef(rve.rotationPitch, 1, 0, 0);
	}

	/**
	 * Setup billboard rendering options.
	 */
	public static void resetBillboardRendering() {
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();;
		GlStateManager.popMatrix();
	}

	/**
	 * reset billboard rendering options.
	 */
	public static void setupBillboardRendering() {
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();
	}

}
