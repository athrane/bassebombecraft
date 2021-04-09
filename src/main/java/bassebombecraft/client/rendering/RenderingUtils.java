package bassebombecraft.client.rendering;

import static bassebombecraft.ClientModConstants.ENTITY_TEXTURE_PATH;
import static bassebombecraft.ClientModConstants.GUI_TEXTURE_PATH;
import static bassebombecraft.ClientModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.ClientModConstants.TEXT_SCALE;
import static bassebombecraft.ModConstants.BILLBOARD_LINE_WIDTH;
import static bassebombecraft.ModConstants.EQUILATERAL_TRIANGLE_HEIGHT;
import static bassebombecraft.ModConstants.MODID;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.ClientModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
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
	public static Vector3d getRenderPos() {

		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
		Vector3d pv = info.getProjectedView();
		Vector3d renderPos = new Vector3d(pv.getX(), pv.getY(), pv.getZ());
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
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture();
	}

	/**
	 * Complete rendering of simple lines.
	 */
	@Deprecated
	public static void completeSimpleRendering() {
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
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
		GlStateManager.enableTexture();
		GlStateManager.enableDepthTest();
	}

	/**
	 * reset billboard rendering options.
	 */
	public static void setupBillboardRendering() {

		// save matrix
		RenderSystem.pushMatrix();

		RenderSystem.disableLighting();
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
	@Deprecated
	public static void renderLineBillboard(Vector3d playerPos, Vector3d entityPos, Vector3d targetPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

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
	@Deprecated
	public static void renderRectangleBillboard(Vector3d playerPos, Vector3d entityPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(1);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

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
	@Deprecated
	public static void renderTriangleBillboard(Vector3d playerPos, Vector3d entityPos, Vector4f rotation) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// translate to camera position
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// add addition rotation
		GlStateManager.rotatef(rotation.getW(), rotation.getX(), rotation.getY(), rotation.getZ());

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
	 * Render a unit coordinate system centered at origin.
	 * 
	 * @param playerPos player position
	 * @param entityPos entity position
	 */
	@Deprecated
	public static void renderDebugBillboard(Vector3d playerPos, Vector3d entityPos) {
		setupBillboardRendering();

		// X/R

		// set line width & color
		GlStateManager.lineWidth(2);
		GlStateManager.color4f(1.0F, 0.0F, 0.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(1.0F, 0, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();

		// Y/G

		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(2);
		GlStateManager.color4f(0.0F, 1.0F, 0.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		tessellator = Tessellator.getInstance();
		bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(0, 1.0F, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();

		// Z/B

		setupBillboardRendering();

		// set line width & color
		GlStateManager.lineWidth(2);
		GlStateManager.color4f(0.0F, 0.0F, 1.0F, 1.0F);

		// translate and rotate billboard
		GlStateManager.translated(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		tessellator = Tessellator.getInstance();
		bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(0, 0, 1.0F).endVertex();

		tessellator.draw();

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
	public static void renderBillboardText(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y,
			String text) {
		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		matrixStack.push();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		matrixStack.translate(0, 0, ClientModConstants.TEXT_Z_TRANSLATION);
		Matrix4f positionMatrix = matrixStack.getLast().getMatrix();
		fontRenderer.renderString(text, x, y, ClientModConstants.TEXT_COLOR, DROP_SHADOW, positionMatrix, buffer,
				IS_TRANSPARENT, TEXT_EFFECT, PACKED_LIGHT);
		matrixStack.pop();

	}

	/**
	 * Render wireframe box.
	 * 
	 * @param aabb AABB to render.
	 */
	@Deprecated
	public static void renderWireframeBox(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		// BC
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		// CD
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		// DA
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		// EF
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		// FG
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		// GH
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		// HE
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		// AE
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		// BF
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		// CG
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		// DH
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	@Deprecated
	public static void renderSolidBox(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// ABCD - bottom (clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

		// ADHE - back (clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		// EFGH - top
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		// BCGF - front
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		// ABFE
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		// CDHG
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render top of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxTop(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// EFGH - top (anti-clockwise)
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render bottom of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxBottom(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// ABCD - bottom (clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render north side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxNorth(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// ADHE - back (clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render south side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxSouth(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// BCGF - front (anti-clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render east side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxEast(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// CDHG (anti-clockwise)
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render west side of solid box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderSolidBoxWest(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// BAEF (clockwise)
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

		tessellator.draw();
	}

	/**
	 * Render line.
	 * 
	 * @param start start position.
	 * @param end   end position.
	 */
	public static void renderLine(Vector3d start, Vector3d end) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(start.x, start.y, start.z).endVertex();
		bufferBuilder.pos(end.x, end.y, end.z).endVertex();
		tessellator.draw();
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
