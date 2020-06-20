package bassebombecraft.client.rendering;

import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.TEXT_SCALE;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 * https://www.minecraftforge.net/forum/topic/81125-solved1152-updating-world-space-rendering-from-112-to-115/
 */
public class DebugRenderer_WorldLastEventText_v1 {
	
	public static void render(RenderWorldLastEvent event) {
		showMobs(event.getMatrixStack());
	}

	static void showMobs(MatrixStack matrixStack) {
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		
		renderText3(matrixStack, buffer, 0, 0, "V3_RENDER_TEXT_V3", TEXT_COLOR, 1F);
		renderText3(matrixStack, buffer, 0, -20, "V3_RENDER_TEXT_V3", TEXT_COLOR, 1F);
		renderText3(matrixStack, buffer, 0, -40, "V3_RENDER_TEXT_V3", TEXT_COLOR, 1F);		
		
	}
	
	/**
	 * NB: Can be seen along the negative z-axis 
	 * in 1/3.person view....
	 * Text is oscillaitng along z- axis.
	 * Text isnt rotation correct around the y axis
	 */
	static void renderText3(MatrixStack matrixStack, IRenderTypeBuffer buffer, double x, double y, String text,
			int color, float scale) {

		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
		FontRenderer fontrenderer = renderManager.getFontRenderer();

		double w = RenderingUtils.oscillate(0, 100);			
				
		matrixStack.push();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);		
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
	    matrixStack.translate(0, 0, w);		
	    
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		fontrenderer.renderString(text, (float) (x * 10 / scale - 40 / scale), (float) (y * 10 / scale - 40 / scale),
				color, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.pop();
	}
	
}
