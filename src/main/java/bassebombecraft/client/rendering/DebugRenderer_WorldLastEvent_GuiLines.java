package bassebombecraft.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.rendering.rendertype.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 * https://www.minecraftforge.net/forum/topic/81125-solved1152-updating-world-space-rendering-from-112-to-115/
 */
public class DebugRenderer_WorldLastEvent_GuiLines {

	public static void render(RenderWorldLastEvent event) {
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		MatrixStack matrixStack = event.getMatrixStack();

		renderRect(matrixStack, buffer, new Vector3f(0, 0, 0), new Vector3f(1, 1, 0));
		renderRect(matrixStack, buffer, new Vector3f(0, 0, 0), new Vector3f(0.5F, 0.5F, 0));
		renderRect(matrixStack, buffer, new Vector3f(-1, -1, 0), new Vector3f(1, 1, 0));

		// To handle bug
		// https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(RenderTypes.OVERLAY_LINES);
	}

	/**
	 * NB: Can be seen 3.person view....
	 */
	static void renderRect(MatrixStack matrixStack, IRenderTypeBuffer buffer, Vector3f pos, Vector3f size) {
		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
		IVertexBuilder builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);

		matrixStack.push();
		// matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		// matrixStack.translate(0, 0, TEXT_Z_TRANSLATION);
		matrixStack.translate(0, 0, 1);
		Matrix4f positionMatrix = matrixStack.getLast().getMatrix();
		drawLine(builder, positionMatrix, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + size.getX(), pos.getY(),
				pos.getZ());
		drawLine(builder, positionMatrix, pos.getX() + size.getX(), pos.getY(), pos.getZ(), pos.getX() + size.getX(),
				pos.getY() + size.getY(), pos.getZ());
		drawLine(builder, positionMatrix, pos.getX() + size.getX(), pos.getY() + size.getY(), pos.getZ(), pos.getX(),
				pos.getY() + size.getY(), pos.getZ());
		drawLine(builder, positionMatrix, pos.getX(), pos.getY() + size.getY(), pos.getZ(), pos.getX(), pos.getY(),
				pos.getZ());
		matrixStack.pop();
	}

	static void drawLine(IVertexBuilder builder, Matrix4f positionMatrix, float dx1, float dy1, float dz1, float dx2,
			float dy2, float dz2) {
		builder.pos(positionMatrix, dx1, dy1, dz1).color(0.0f, 0.0f, 1.0f, 1.0f).endVertex();
		builder.pos(positionMatrix, dx2, dy2, dz2).color(0.0f, 0.0f, 1.0f, 1.0f).endVertex();
	}

}
