package bassebombecraft.client.rendering;

import static bassebombecraft.ClientModConstants.ENTITY_TEXTURE_PATH;
import static bassebombecraft.ClientModConstants.GUI_TEXTURE_PATH;
import static bassebombecraft.ClientModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.ClientModConstants.TEXT_SCALE;
import static bassebombecraft.ModConstants.BILLBOARD_LINE_WIDTH;
import static bassebombecraft.ModConstants.EQUILATERAL_TRIANGLE_HEIGHT;
import static bassebombecraft.ModConstants.MODID;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.ClientModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering utilities.
 */
public class RenderingUtils {

	/**
	 * Packed light
	 */
	static final int PACKED_LIGHT = 0xf000f0;

	/**
	 * Some text effect.
	 */
	static final int TEXT_EFFECT = 0;

	/**
	 * Text isn't rendered transparent.
	 */
	static final boolean IS_TRANSPARENT = false;

	/**
	 * Render text with no drop shadow.
	 */
	static final boolean DROP_SHADOW = false;

	/**
	 * Get render position from {@linkplain EntityRendererManager}.
	 * 
	 * The value are private fields which are accessed using reflection.
	 * 
	 * @return render position.
	 */
	@Deprecated
	public static Vec3 getRenderPos() {

		Minecraft mc = Minecraft.getInstance();
		Camera info = mc.gameRenderer.getMainCamera();
		Vec3 pv = info.getPosition();
		Vec3 renderPos = new Vec3(pv.x(), pv.y(), pv.z());
		return renderPos;
	}

	/**
	 * Prepare GL for rendering of simple lines.
	 * 
	 * @param x x-coordinate for translation.
	 * @param y y-coordinate for translation.
	 * @param z z-coordinate for translation.
	 */
	@Deprecated
	public static void prepareSimpleRendering(double x, double y, double z) {
		GlStateManager._pushMatrix();
		GlStateManager._translated(x, y, z);
		GlStateManager._disableLighting();
		GlStateManager._enableBlend();
		GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager._disableTexture();
	}

	/**
	 * Complete rendering of simple lines.
	 */
	@Deprecated
	public static void completeSimpleRendering() {
		GlStateManager._enableTexture();
		GlStateManager._disableBlend();
		GlStateManager._enableLighting();
		GlStateManager._popMatrix();
	}

	/**
	 * Setup billboard rotation to face camera.
	 */
	public static void setupBillboardRotation() {
		RenderSystem.rotatef(180, 0, 1, 0);
	}

	/**
	 * Setup billboard rendering options.
	 */
	@Deprecated
	public static void resetBillboardRendering() {

		// get matrix
		RenderSystem.popMatrix();

		// set attributes
		RenderSystem.enableLighting();
		GlStateManager._enableTexture();
		GlStateManager._enableDepthTest();
	}

	/**
	 * reset billboard rendering options.
	 */
	public static void setupBillboardRendering() {

		// save matrix
		RenderSystem.pushMatrix();

		RenderSystem.disableLighting();
		GlStateManager._disableTexture();
		GlStateManager._disableDepthTest();
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
	@Deprecated
	public static void renderLineBillboard(Vec3 playerPos, Vec3 entityPos, Vec3 targetPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager._lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// translate to camera position
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// ET
		bufferBuilder.vertex(0, 0, 0).endVertex();
		bufferBuilder.vertex(targetPos.x - entityPos.x, targetPos.y - entityPos.y, targetPos.z - entityPos.z).endVertex();

		tessellator.end();

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
	@Deprecated
	public static void renderRectangleBillboard(Vec3 playerPos, Vec3 entityPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager._lineWidth(1);
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(0 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.vertex(1 - 0.5F, 0 - 0.5F, 0).endVertex();

		// BC
		bufferBuilder.vertex(1 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.vertex(1 - 0.5F, 1 - 0.5F, 0).endVertex();

		// CD
		bufferBuilder.vertex(1 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.vertex(0 - 0.5F, 1 - 0.5F, 0).endVertex();

		// DA
		bufferBuilder.vertex(0 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.vertex(0 - 0.5F, 0 - 0.5F, 0).endVertex();

		tessellator.end();

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
	@Deprecated
	public static void renderTriangleBillboard(Vec3 playerPos, Vec3 entityPos, Vector4f rotation) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager._lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// translate to camera position
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// add addition rotation
		GlStateManager._rotatef(rotation.w(), rotation.x(), rotation.y(), rotation.z());

		// create tessellator & bufferbuilder
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(0 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.vertex(1 - 0.5F, 0 - 0.289F, 0).endVertex();

		// BC
		bufferBuilder.vertex(1 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.vertex(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();

		// CA
		bufferBuilder.vertex(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();
		bufferBuilder.vertex(0 - 0.5F, -0.289F, 0).endVertex();

		tessellator.end();

		resetBillboardRendering();
	}

	/**
	 * Render a unit coordinate system centered at origin.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 */
	@Deprecated
	public static void renderDebugBillboard(Vec3 playerPos, Vec3 entityPos) {
		setupBillboardRendering();

		// X/R

		// set line width & color
		GlStateManager._lineWidth(2);
		GlStateManager._color4f(1.0F, 0.0F, 0.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(0, 0, 0).endVertex();
		bufferBuilder.vertex(1.0F, 0, 0).endVertex();

		tessellator.end();

		resetBillboardRendering();

		// Y/G

		setupBillboardRendering();

		// set line width & color
		GlStateManager._lineWidth(2);
		GlStateManager._color4f(0.0F, 1.0F, 0.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		tessellator = Tesselator.getInstance();
		bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(0, 0, 0).endVertex();
		bufferBuilder.vertex(0, 1.0F, 0).endVertex();

		tessellator.end();

		resetBillboardRendering();

		// Z/B

		setupBillboardRendering();

		// set line width & color
		GlStateManager._lineWidth(2);
		GlStateManager._color4f(0.0F, 0.0F, 1.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager._translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		tessellator = Tesselator.getInstance();
		bufferBuilder = tessellator.getBuilder();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(0, 0, 0).endVertex();
		bufferBuilder.vertex(0, 0, 1.0F).endVertex();

		tessellator.end();

		resetBillboardRendering();

	}

	/**
	 * Render billboard text.
	 * 
	 * Supports rendering of billboard text in the renderer instances handling
	 * processing the {@linkplain RenderWorldLastEvent}.
	 * 
	 * @param matrixStack matrix stack
	 * @param buffer      render buffer.
	 * @param x           x coordinate for text placement.
	 * @param x           y coordinate for text placement.
	 * @param text        text to render.
	 */
	@Deprecated
	public static void renderBillboardText(PoseStack matrixStack, MultiBufferSource buffer, float x, float y,
			String text) {
		EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		Font fontRenderer = renderManager.getFont();

		matrixStack.pushPose();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.mulPose(renderManager.cameraOrientation());
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
		matrixStack.translate(0, 0, ClientModConstants.TEXT_Z_TRANSLATION);
		Matrix4f positionMatrix = matrixStack.last().pose();
		fontRenderer.drawInBatch(text, x, y, ClientModConstants.TEXT_COLOR, DROP_SHADOW, positionMatrix, buffer,
				IS_TRANSPARENT, TEXT_EFFECT, PACKED_LIGHT);
		matrixStack.popPose();

	}

	/**
	 * Render wireframe box.
	 * 
	 * @param aabb AABB to render.
	 */
	@Deprecated
	public static void renderWireframeBox(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

		// AB
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		// BC
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		// CD
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		// DA
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		// EF
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		// FG
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		// GH
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		// HE
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		// AE
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		// BF
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		// CG
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		// DH
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	@Deprecated
	public static void renderSolidBox(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// ABCD - bottom (clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

		// ADHE - back (clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		// EFGH - top
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		// BCGF - front
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		// ABFE
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		// CDHG
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render top of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxTop(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// EFGH - top (anti-clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render bottom of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxBottom(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// ABCD - bottom (clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render north side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxNorth(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// ADHE - back (clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render south side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxSouth(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// BCGF - front (anti-clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render east side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxEast(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// CDHG (anti-clockwise)
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render west side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxWest(AABB aabb) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		// BAEF (clockwise)
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.vertex(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.end();
	}

	/**
	 * Render line.
	 * 
	 * @param start start position.
	 * @param end   end position.
	 */
	public static void renderLine(Vec3 start, Vec3 end) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(start.x, start.y, start.z).endVertex();
		bufferBuilder.vertex(end.x, end.y, end.z).endVertex();
		tessellator.end();
	}

	/**
	 * Create entity texture resource location for projectile entity.
	 * 
	 * @param projectileEntity class name of entity.
	 * 
	 * @return texture resource location for entity.
	 */
	public static ResourceLocation createEntityTextureResourceLocation(Class<?> projectileEntity) {
		String textureName = new StringBuilder().append(ENTITY_TEXTURE_PATH)
				.append(projectileEntity.getSimpleName().toLowerCase()).append(".png").toString();
		return new ResourceLocation(MODID, textureName);
	}

	/**
	 * Create texture resource location for GUI screen.
	 * 
	 * @param containerScreen class name of GUI screen.
	 * 
	 * @return texture resource location for GUI screen.
	 */
	public static ResourceLocation createGuiTextureResourceLocation(Class<?> containerScreen) {
		String textureName = new StringBuilder().append(GUI_TEXTURE_PATH)
				.append(containerScreen.getSimpleName().toLowerCase()).append(".png").toString();
		return new ResourceLocation(MODID, textureName);
	}

	/**
	 * Create texture resource location for GUI.
	 * 
	 * @param guiTexture name of GUI texture.
	 * 
	 * @return texture resource location for GUI texture.
	 */
	public static ResourceLocation createGuiTextureResourceLocation(String guiTexture) {
		String textureName = new StringBuilder().append(GUI_TEXTURE_PATH)
				.append(guiTexture.toLowerCase()).append(".png").toString();
		return new ResourceLocation(MODID, textureName);
	}
	
}
