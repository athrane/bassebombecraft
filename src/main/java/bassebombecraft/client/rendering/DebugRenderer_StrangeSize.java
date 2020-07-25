package bassebombecraft.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraftforge.client.event.RenderLivingEvent;
import static bassebombecraft.geom.GeometryUtils.oscillate;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 */
public class DebugRenderer_StrangeSize {

	public static void render(RenderLivingEvent.Pre event) {

		MatrixStack matrixStack = event.getMatrixStack();
		float w = (float) oscillate(0.5F, 2.0F);
		matrixStack.scale(w, w, w);
	}
}
