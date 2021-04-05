package bassebombecraft.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.rendering.rendertype.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraftforge.client.event.RenderLivingEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 */
public class DebugRenderer_MobLines {

	public static void render(RenderLivingEvent.Post event) {
		showMobs(event.getMatrixStack(), event.getEntity());
	}

	private static void greenLine(IVertexBuilder builder, Matrix4f positionMatrix, float dx1, float dy1, float dz1,
			float dx2, float dy2, float dz2) {
		builder.pos(positionMatrix, dx1, dy1, dz1).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
		builder.pos(positionMatrix, dx2, dy2, dz2).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
	}

	private static void redLine(IVertexBuilder builder, Matrix4f positionMatrix, float dx1, float dy1, float dz1,
			float dx2, float dy2, float dz2) {
		builder.pos(positionMatrix, dx1, dy1, dz1).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();
		builder.pos(positionMatrix, dx2, dy2, dz2).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();
	}

	private static void showMobs(MatrixStack matrixStack, LivingEntity entity) {
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);

		Matrix4f positionMatrix = matrixStack.getLast().getMatrix();

		if (entity instanceof IMob) {
			redLine(builder, positionMatrix, 0, .5f, 0, 0, 6, 0);
		} else {
			greenLine(builder, positionMatrix, 0, .5f, 0, 0, 6, 0);
		}
	}
}
