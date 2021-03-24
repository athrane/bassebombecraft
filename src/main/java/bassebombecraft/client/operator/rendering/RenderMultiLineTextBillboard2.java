package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.ClientModConstants.TEXT_SCALE_2;
import static bassebombecraft.ClientModConstants.TEXT_Z_TRANSLATION;
import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.operator.DefaultPorts.getFnMaxtrixStack1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a text
 * billboard.
 * 
 * Supports rendering of billboard text in the renderer instances handling
 * processing the {@linkplain RenderWorldLastEvent}.
 */
public class RenderMultiLineTextBillboard2 implements Operator2 {

	/**
	 * Packed light
	 */
	static final int PACKED_LIGHT = 0xf000f0;

	/**
	 * Some text effect.
	 */
	static final int TEXT_EFFECT = 0;

	/**
	 * Text isn't rendered transparent.
	 */
	static final boolean IS_TRANSPARENT = false;

	/**
	 * Render text with no drop shadow.
	 */
	static final boolean DROP_SHADOW = false;

	/**
	 * Y position delta.
	 */
	static final int Y_DELTA = 10;

	/**
	 * oscillate max value.
	 */
	float oscillateMax;

	/**
	 * Function get messages to render.
	 */
	Function<Ports, Stream<String>> fnGetString;

	/**
	 * Function to get matrix stack.
	 */
	Function<Ports, MatrixStack> fnGetMatrixStack;

	/**
	 * X coordinate for placement of billboard.
	 */
	int x;

	/**
	 * Y coordinate for placement of billboard.
	 */
	int y;

	/**
	 * Text color.
	 */
	int textColor;

	/**
	 * Constructor.
	 * 
	 * @param fnGetString function to get messages.
	 * @param x           x coordinate for placement of billboard.
	 * @param y           y coordinate for placement of billboard.
	 */
	public RenderMultiLineTextBillboard2(Function<Ports, Stream<String>> fnGetString, int x, int y) {
		this(fnGetString, x, y, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetString  function to get messages.
	 * @param x            x coordinate for placement of billboard.
	 * @param y            y coordinate for placement of billboard.
	 * @param oscillateMax oscillate max value
	 */
	public RenderMultiLineTextBillboard2(Function<Ports, Stream<String>> fnGetString, int x, int y,
			float oscillateMax) {
		this(fnGetString, x, y, oscillateMax, TEXT_COLOR);
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetString  function to get messages.
	 * @param x            x coordinate for placement of billboard.
	 * @param y            y coordinate for placement of billboard.
	 * @param oscillateMax oscillate max value
	 * @param textColor    text color.
	 */
	public RenderMultiLineTextBillboard2(Function<Ports, Stream<String>> fnGetString, int x, int y, float oscillateMax,
			int textColor) {
		this.fnGetString = fnGetString;
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.x = x;
		this.y = y;
		this.oscillateMax = oscillateMax;
		this.textColor = textColor;
	}

	@Override
	public void run(Ports ports) {
		MatrixStack matrixStack = applyV(fnGetMatrixStack, ports);
		Stream<String> messages = applyV(fnGetString, ports);

		// get render buffer
		Minecraft mcClient = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffer = mcClient.getRenderTypeBuffers().getBufferSource();

		// get rendering engine
		EntityRendererManager renderManager = mcClient.getRenderManager();
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		// push matrix
		matrixStack.push();

		// setup matrix
		matrixStack.scale(TEXT_SCALE_2, TEXT_SCALE_2, TEXT_SCALE_2);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		double zTranslation = TEXT_Z_TRANSLATION + oscillate(0, oscillateMax);
		matrixStack.translate(0, 0, zTranslation);
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();

		// create index to use inside loop
		final AtomicInteger index = new AtomicInteger();

		// render messages
		messages.forEach(m -> {
			int ypos = y + (Y_DELTA * index.incrementAndGet());
			fontRenderer.renderString(m, x, ypos, textColor, DROP_SHADOW, positionMatrix, buffer, IS_TRANSPARENT,
					TEXT_EFFECT, PACKED_LIGHT);
		});

		// restore matrix
		matrixStack.pop();
	}

}
