package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.BILLBOARD_LINE_WIDTH;
import static bassebombecraft.ModConstants.EQUILATERAL_TRIANGLE_HEIGHT;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.TEXT_SCALE;
import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue;

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
	 * Get render position from {@linkplain EntityRendererManager}.
	 * 
	 * The value are private fields which are accessed using reflection.
	 * 
	 * @return render position.
	 */
	public static Vec3d getRenderPos() {

		// get render manager
		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();

		// get value from private fields
		double renderPosX = getPrivateValue(EntityRendererManager.class, renderManager, "field_78725_b");
		double renderPosY = getPrivateValue(EntityRendererManager.class, renderManager, "field_78726_c");
		double renderPosZ = getPrivateValue(EntityRendererManager.class, renderManager, "field_78723_d");
		Vec3d renderPos = new Vec3d(renderPosX, renderPosY, renderPosZ);

		return renderPos;
	}

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

		// get matrix
		GlStateManager.popMatrix();

		// set attribute
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();
	}

	/**
	 * reset billboard rendering options.
	 */
	public static void setupBillboardRendering() {

		// save matrix
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();

		// GlStateManager.pushMatrix();
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
	 * @param text        text to render
	 * @param rotation    rotation
	 */
	public static void renderTextBillboardV2(Vec3d translation, String text, Vector4f rotation) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// translate to camera position
		GlStateManager.translated(translation.x, translation.y, translation.z);

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
	 * Render text at origin for rendering of HUD text.
	 * 
	 * This method supports translation of the text relative to the player view
	 * direction and independent of the camera (or player) orientation and
	 * placement.
	 * 
	 * @param cameraTranslation camera translation vector.
	 * @param textTranslation   text translation vector for translation of text
	 *                          relative to view direction. Defines the placement of
	 *                          the HUD text.
	 * @param text              text to render
	 */
	public static void renderHudTextBillboard(Vec3d cameraTranslation, Vec3d textTranslation, String text) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// translate to camera position
		GlStateManager.translated(cameraTranslation.x, cameraTranslation.y, cameraTranslation.z);

		// set up billboard rotation
		setupBillboardRotation();

		// translation of text relative to view direction.
		// Defines the placement of the HUD text
		GlStateManager.translated(textTranslation.x, textTranslation.y, textTranslation.z);

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add billboard rotation for text readability
		GlStateManager.rotatef(TEXT_BILLBOARD_ROTATION.w, TEXT_BILLBOARD_ROTATION.x, TEXT_BILLBOARD_ROTATION.y,
				TEXT_BILLBOARD_ROTATION.z);

		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		resetBillboardRendering();
	}

	/**
	 * Render rotated text at origin for rendering compass.
	 * 
	 * This method supports rotation of the text relative to the player view
	 * direction and independent of the camera (or) player orientation and
	 * placement. *
	 * 
	 * @param cameraTranslation camera translation vector.
	 * @param text              text to render
	 * @param rotation          rotation rotation vector for rotation of text
	 *                          relative to view direction.
	 */
	public static void renderRotatedTextBillboard(Vec3d cameraTranslation, Vector4f rotation, String text) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// add addition rotation
		GlStateManager.rotatef(rotation.w, rotation.x, rotation.y, rotation.z);

		// translate to camera position
		GlStateManager.translated(cameraTranslation.x, cameraTranslation.y, cameraTranslation.z);

		// add addition rotation
		GlStateManager.rotatef(rotation.w, -rotation.x, -rotation.y, -rotation.z);

		// set up billboard rotation
		setupBillboardRotation();

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add billboard rotation for text readability
		GlStateManager.rotatef(TEXT_BILLBOARD_ROTATION.w, TEXT_BILLBOARD_ROTATION.x, TEXT_BILLBOARD_ROTATION.y,
				TEXT_BILLBOARD_ROTATION.z);

		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		resetBillboardRendering();
	}

	/**
	 * Render a box.
	 */
	public static void renderBox(Vec3d translation, double dx, double dy, double dz, Vec3d pos) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.setTranslation(translation.getX(), translation.getY(), translation.getZ());
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(pos.x, pos.y, pos.z).endVertex();
		bufferBuilder.pos(pos.x, pos.y, pos.z + dz).endVertex();
		// BC
		bufferBuilder.pos(pos.x, pos.y, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z + dz).endVertex();
		// CD
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z).endVertex();
		// DA
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z).endVertex();
		bufferBuilder.pos(pos.x, pos.y, pos.z).endVertex();
		// EF
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z).endVertex();
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z + dz).endVertex();
		// FG
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z + dz).endVertex();
		// GH
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z).endVertex();
		// HE
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z).endVertex();
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z).endVertex();
		// AE
		bufferBuilder.pos(pos.x, pos.y, pos.z).endVertex();
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z).endVertex();
		// BF
		bufferBuilder.pos(pos.x, pos.y, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x, pos.y + dy, pos.z + dz).endVertex();
		// CG
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z + dz).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z + dz).endVertex();
		// DH
		bufferBuilder.pos(pos.x + dx, pos.y, pos.z).endVertex();
		bufferBuilder.pos(pos.x + dx, pos.y + dy, pos.z).endVertex();

		tessellator.draw();
		bufferBuilder.setTranslation(0, 0, 0);
	}

}
