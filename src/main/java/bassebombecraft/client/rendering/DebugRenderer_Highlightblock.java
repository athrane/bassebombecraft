package bassebombecraft.client.rendering;

import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.TEXT_SCALE;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.rendering.rendertype.OverlayLines;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 * 
 * Text is rendered as billboard on top of entity. Text is rotated 180D around
 * the Z-axis (to get on top of the entity).
 * 
 * MatrixStack is read from entity. Experimentation with culling.
 */
public class DebugRenderer_Highlightblock {

	public static void render(HighlightBlock event) {

		// exit if player is undefined
		if (!isClientSidePlayerDefined())
			return;

		// get player
		PlayerEntity player = getClientSidePlayer();

		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(OverlayLines.OVERLAY_LINES);

		BlockRayTraceResult blockResult = (BlockRayTraceResult) event.getTarget();
		BlockPos blockPos = blockResult.getPos();
		AxisAlignedBB aabb = new AxisAlignedBB(blockPos);
		double w = RenderingUtils.oscillate(0, 0.02F);
		aabb = aabb.grow(w);

		Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

		// get world
		World world = player.world;

		// Get block type
		BlockState blockstate = world.getBlockState(blockPos);
		String message = blockstate.getBlock().getNameTextComponent().getUnformattedComponentText();

		MatrixStack matrixStack = event.getMatrix();
		matrixStack.push();
		matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		renderWireframeBox(aabb, builder, positionMatrix);
		matrixStack.pop();

		// see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(OverlayLines.OVERLAY_LINES);

		renderText_default(matrixStack, buffer, 0, 0, message);

		// see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(OverlayLines.OVERLAY_LINES);
	}

	/**
	 * NB: Can be seen 3.person view....
	 */
	static void renderText_default(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y, String text) {
		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
		FontRenderer fontrenderer = renderManager.getFontRenderer();

		matrixStack.push();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		matrixStack.translate(0, 0, 100);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		fontrenderer.renderString(text, x, y, TEXT_COLOR, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.pop();

	}

	/**
	 * Render wireframe box.
	 * 
	 * @param aabb AABB to render.
	 */
	public static void renderWireframeBox(AxisAlignedBB aabb, IVertexBuilder builder, Matrix4f positionMatrix) {

		// AB
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		// BC
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		// CD
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);

		// DA
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		// EF
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// FG
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// GH
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		// HE
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// AE
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.minZ);
		// BF
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.minX, aabb.maxY, aabb.maxZ);
		// CG
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.maxZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.maxZ);
		// DH
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.minY, aabb.minZ);
		addWhiteVertex(builder, positionMatrix, aabb.maxX, aabb.maxY, aabb.minZ);

	}

	static void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.pos(positionMatrix, x, y, z).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
	}

	static void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z) {
		addWhiteVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
