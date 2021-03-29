package bassebombecraft.client.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.ClientModConstants.TEXT_SCALE;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 * https://www.minecraftforge.net/forum/topic/81125-solved1152-updating-world-space-rendering-from-112-to-115/
 */
public class DebugRenderer_WorldLastEventText {

	public static void render(RenderWorldLastEvent event) {
		showMobs(event.getMatrixStack());
	}

	static void showMobs(MatrixStack matrixStack) {
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

		renderText_default(matrixStack, buffer, 0, -10, "(0,-10)");
		renderText_default(matrixStack, buffer, 0, 0, "(0,0)");
		renderText_default(matrixStack, buffer, 0, 10, "(0,10)");
		renderText_default(matrixStack, buffer, 0, 20, "(0,20)");
		renderText_default(matrixStack, buffer, 0, 40, "(0,40)");
		renderText_default(matrixStack, buffer, 20, 0, "(20,0)");
		renderText_default(matrixStack, buffer, -20, 0, "(-20,0)");
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
		matrixStack.translate(0, 0, 200);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		fontrenderer.renderString(text, x, y, TEXT_COLOR, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.pop();
	}

}
