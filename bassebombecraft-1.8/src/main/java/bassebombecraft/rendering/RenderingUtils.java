package bassebombecraft.rendering;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

/**
 * Rendering utilities.
 */
public class RenderingUtils {

	/**
	 * Setup billboard rotation to face camera.
	 */
	public static void setupBillboardRotation() {
		final Minecraft mc = Minecraft.getMinecraft();		
		RenderManager renderManager = mc.getRenderManager();
		GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	}		
	
	/**
	 * Setup billboard rotation to face camera.
	 */	
	public static void setupBillboardRotationV2() {
		final Minecraft mc = Minecraft.getMinecraft();		
	    final Entity rve = mc.getRenderViewEntity();
		GlStateManager.rotate(-rve.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(rve.rotationPitch, 1, 0, 0);
	}

	/**
	 * Setup billboard rendering options.
	 */
	public static void resetBillboardRendering() {
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();		
		GlStateManager.popMatrix();
	}

	/**
	 * reset billboard rendering options.
	 */	
	public static void setupBillboardRendering() {
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
	}	
	
}
