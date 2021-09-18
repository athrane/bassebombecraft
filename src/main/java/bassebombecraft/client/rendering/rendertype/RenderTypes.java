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

	public RenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn,
			boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	static final LineState THICK_LINES = new LineState(OptionalDouble.of(3.0D));

	static final LineState THIN_LINES = new LineState(OptionalDouble.of(0.5D));

	static final LineState MEDIUM_LINES = new LineState(OptionalDouble.of(1.0D));
	
	public static final RenderType OVERLAY_LINES = makeType("overlay_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.State.getBuilder().line(THICK_LINES).transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE)
					.depthTest(DEPTH_ALWAYS).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE)
					.build(false));

	public static final RenderType DEFAULT_LINES = makeType("default_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.State.getBuilder().line(THICK_LINES).transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE)
					.depthTest(DEPTH_ALWAYS).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE)
					.build(false));

	public static final RenderType LIGHTNING_LINES = makeType("lightning_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256, RenderType.State.getBuilder().line(THICK_LINES).transparency(TRANSLUCENT_TRANSPARENCY)
					.texture(NO_TEXTURE).writeMask(COLOR_WRITE).build(false));

	public static final RenderType PROJECTILE_TRAIL_LINES = makeType("projectile_trail_lines",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.State.getBuilder().line(THIN_LINES)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).writeMask(COLOR_WRITE).build(false));

	public static final RenderType WILDFIRE_LINES = makeType("wildfire_lines",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.State.getBuilder().line(MEDIUM_LINES)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).writeMask(COLOR_WRITE).build(false));

	public static final RenderType CONTAGION_LINES = makeType("contagion_lines",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.State.getBuilder().line(MEDIUM_LINES)
					.transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).writeMask(COLOR_WRITE).build(false));
	
	public static final RenderType SIMPLE_LINES = makeType("simple_lines", DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES, 256, RenderType.State.getBuilder().line(THIN_LINES).build(false));

}
