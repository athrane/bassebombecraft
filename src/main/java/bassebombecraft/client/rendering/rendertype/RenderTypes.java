package bassebombecraft.client.rendering.rendertype;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderStateShard.LineStateShard;

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

	static final LineStateShard THICK_LINES = new LineStateShard(OptionalDouble.of(3.0D));

	static final LineStateShard THIN_LINES = new LineStateShard(OptionalDouble.of(0.5D));

	static final LineStateShard MEDIUM_LINES = new LineStateShard(OptionalDouble.of(1.0D));
	
	public static final RenderType OVERLAY_LINES = create("overlay_lines", DefaultVertexFormat.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.CompositeState.builder().setLineState(THICK_LINES).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE)
					.setDepthTestState(NO_DEPTH_TEST).setCullState(NO_CULL).setLightmapState(NO_LIGHTMAP).setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false));

	public static final RenderType DEFAULT_LINES = create("default_lines", DefaultVertexFormat.POSITION_COLOR,
			GL11.GL_LINES, 256,
			RenderType.CompositeState.builder().setLineState(THICK_LINES).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE)
					.setDepthTestState(NO_DEPTH_TEST).setCullState(NO_CULL).setLightmapState(NO_LIGHTMAP).setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false));

	public static final RenderType LIGHTNING_LINES = create("lightning_lines", DefaultVertexFormat.POSITION_COLOR,
			GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(THICK_LINES).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setTextureState(NO_TEXTURE).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

	public static final RenderType PROJECTILE_TRAIL_LINES = create("projectile_trail_lines",
			DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(THIN_LINES)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

	public static final RenderType WILDFIRE_LINES = create("wildfire_lines",
			DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(MEDIUM_LINES)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

	public static final RenderType CONTAGION_LINES = create("contagion_lines",
			DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(MEDIUM_LINES)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

	public static final RenderType CONTAGION_SPREAD_LINES = create("contagion_spread_lines",
			DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(THICK_LINES)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
	
	public static final RenderType SIMPLE_LINES = create("simple_lines", DefaultVertexFormat.POSITION_COLOR,
			GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(THIN_LINES).createCompositeState(false));

}
