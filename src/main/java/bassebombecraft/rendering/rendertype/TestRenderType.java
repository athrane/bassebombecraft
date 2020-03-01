package bassebombecraft.rendering.rendertype;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

/**
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 */
public class TestRenderType extends RenderType {

	 public TestRenderType(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
	        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
	    }

	    private static final LineState THICK_LINES = new LineState(OptionalDouble.of(3.0D));

	    public static final RenderType OVERLAY_LINES = get("overlay_lines",
	            DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
	            RenderType.State.builder().line(THICK_LINES)
	                    .layer(PROJECTION_LAYERING)
	                    .transparency(TRANSLUCENT_TRANSPARENCY)
	                    .texture(NO_TEXTURE)
	                    .depthTest(DEPTH_ALWAYS)
	                    .cull(CULL_DISABLED)
	                    .lightmap(LIGHTMAP_DISABLED)
	                    .writeMask(COLOR_WRITE)
	                    .build(false));	
}
