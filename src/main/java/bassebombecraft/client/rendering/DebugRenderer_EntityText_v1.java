package bassebombecraft.client.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.ClientModConstants.TEXT_SCALE;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.rendering.rendertype.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 * 
 * Can be seen along negativ Z-axis.
 * 
 * MatrixStack is read from entity.
 */
public class DebugRenderer_EntityText_v1 {

	public static void render(RenderLivingEvent.Post event) {
		showMobs(event.getMatrixStack(), event.getEntity());
	}

	static void showMobs(MatrixStack matrixStack, LivingEntity entity) {
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);

		renderText(matrixStack, buffer, 0, 0, "RENDER_TEXT V1", TEXT_COLOR, 1F);
	}

	static void renderText(MatrixStack matrixStack, IRenderTypeBuffer buffer, double x, double y, String text,
			int color, float scale) {
		matrixStack.push();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		Minecraft.getInstance().fontRenderer.renderString(text, (float) (x * 10 / scale - 40 / scale),
				(float) (y * 10 / scale - 40 / scale), color, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.pop();
	}

}
