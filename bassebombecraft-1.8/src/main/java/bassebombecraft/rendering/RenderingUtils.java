package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.BILLBOARD_LINE_WIDTH;
import static bassebombecraft.ModConstants.EQUILATERAL_TRIANGLE_HEIGHT;
import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.TEXT_SCALE;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

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
		GlStateManager.enableDepthTest();
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

	/**
	 * Render a rectangle billboard centered at origin.
	 * 
	 * The billboard is drawn as a line from entity to target.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 * @param targetPos target position
	 */
	public static void renderLineBillboard(Vec3d playerPos, Vec3d entityPos, Vec3d targetPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);

		// translate to camera position
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// ET
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(targetPos.x - entityPos.x, targetPos.y - entityPos.y, targetPos.z - entityPos.z).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render a rectangle billboard centered at origin.
	 * 
	 * The billboard is drawn as a rectangle around the target entity.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 */
	public static void renderRectangleBillboard(Vec3d playerPos, Vec3d entityPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(1);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 0 - 0.5F, 0).endVertex();

		// BC
		bufferBuilder.pos(1 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 1 - 0.5F, 0).endVertex();

		// CD
		bufferBuilder.pos(1 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, 1 - 0.5F, 0).endVertex();

		// DA
		bufferBuilder.pos(0 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, 0 - 0.5F, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render a equilateral triangle billboard centered at origin.
	 * 
	 * The billboard is drawn as a triangle around the entity.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 * @param rotation  rotation
	 */
	public static void renderTriangleBillboard(Vec3d playerPos, Vec3d entityPos, Vector4f rotation) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);

		// translate to camera position
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// add addition rotation
		GlStateManager.rotatef(rotation.w, rotation.x, rotation.y, rotation.z);

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 0 - 0.289F, 0).endVertex();

		// BC
		bufferBuilder.pos(1 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.pos(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();

		// CA
		bufferBuilder.pos(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, -0.289F, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render text at origin.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 * @param text      text to render
	 * @param rotation  rotation
	 */
	public static void renderTextBillboard(Vec3d playerPos, Vec3d entityPos, String text, Vector4f rotation) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// translate to camera position
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add addition rotation
		GlStateManager.rotatef(rotation.w, rotation.x, rotation.y, rotation.z);

		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		resetBillboardRendering();
	}

	/**
	 * Render text at origin.
	 * 
	 * @param translation translation vector.
	 * @param text      text to render
	 * @param rotation  rotation
	 */
	public static void renderTextBillboard(Vec3d translation, String text, Vector4f rotation) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// translate to camera position
		GlStateManager.translated(translation.x, translation.y, translation.z);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		
		// set up billboard rotation
		//setupBillboardRotation();
		EntityRendererManager renderManager = mc.getRenderManager();
		GlStateManager.rotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add addition rotation
		GlStateManager.rotatef(rotation.w, rotation.x, rotation.y, rotation.z);

		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		resetBillboardRendering();
	}	
}
