package bassebombecraft.client.rendering.rendertype;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

/**
 * Render types used for rendering.
 * 
 * {@linkplain RenderType} definition for overlay lines. see:
 * https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
 */
public class RenderTypes extends RenderType {

	public RenderTypes(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_,
			boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
		super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
	}

	static final LineState THICK_LINES = new LineState(OptionalDouble.of(3.0D));

	static final LineState THIN_LINES = new LineState(OptionalDouble.of(0.5D));

	public static final RenderType OVERLAY_LINES = get("overlay_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.State.builder().line(THICK_LINES).layer(PROJECTION_LAYERING)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
					.cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false));

	public static final RenderType DEFAULT_LINES = get("default_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.State.builder().line(THICK_LINES).layer(PROJECTION_LAYERING)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
					.cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false));

	public static final RenderType LIGHTNING_LINES = get("lightning_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.State.builder().line(THICK_LINES).layer(PROJECTION_LAYERING)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
					.cull(CULL_ENABLED).writeMask(COLOR_WRITE).build(false));

	public static final RenderType PROJECTILE_TRAIL_LINES = get("projectile_trail_lines",
			DefaultVertexFormats.POSITION_COLOR, 
			GL11.GL_LINES, 
			256,
			RenderType.State.builder()
			.line(THIN_LINES)
            .transparency(TRANSLUCENT_TRANSPARENCY)
            .depthTest(DEPTH_ALWAYS)
            .build(false));

	public static final RenderType PROJECTILE_TRAIL_LINES2 = get("projectile_trail_lines",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
			RenderType.State.builder().line(THIN_LINES).layer(PROJECTION_LAYERING)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
					.cull(CULL_ENABLED).writeMask(COLOR_DEPTH_WRITE).build(false));

	public static final RenderType SIMPLE_LINES = get("simple_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256, RenderType.State.builder().line(THIN_LINES).build(false));

}
