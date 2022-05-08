package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.ClientModConstants.TEXT_SCALE_2;
import static bassebombecraft.ClientModConstants.TEXT_Z_TRANSLATION;
import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which renders a text
 * billboard.
 * 
 * Supports rendering of billboard text in the renderer instances processing the
 * {@linkplain RenderWorldLastEvent}.
 */
public class RenderTextBillboard2 implements Operator2 {

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
	 * oscillate max value.
	 */
	float oscillateMax;

	/**
	 * Function get message to render.
	 */
	Function<Ports, String> fnGetString;

	/**
	 * Function to get matrix stack.
	 */
	Function<ClientPorts, PoseStack> fnGetMatrixStack;

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
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * @param fnGetString function to get message.
	 * @param x           x coordinate for placement of billboard.
	 * @param y           y coordinate for placement of billboard.
	 */
	public RenderTextBillboard2(Function<Ports, String> fnGetString, int x, int y) {
		this(fnGetString, x, y, 0);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * @param fnGetString  function to get message.
	 * @param x            x coordinate for placement of billboard.
	 * @param y            y coordinate for placement of billboard.
	 * @param oscillateMax oscillate max value
	 */
	public RenderTextBillboard2(Function<Ports, String> fnGetString, int x, int y, float oscillateMax) {
		this(fnGetString, x, y, oscillateMax, TEXT_COLOR);
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetString  function to get message.
	 * @param x            x coordinate for placement of billboard.
	 * @param y            y coordinate for placement of billboard.
	 * @param oscillateMax oscillate max value
	 * @param textColor    text color.
	 */
	public RenderTextBillboard2(Function<Ports, String> fnGetString, int x, int y, float oscillateMax, int textColor) {
		this.fnGetString = fnGetString;
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.x = x;
		this.y = y;
		this.oscillateMax = oscillateMax;
		this.textColor = textColor;
	}

	@Override
	public void run(Ports ports) {
		PoseStack matrixStack = clientApplyV(fnGetMatrixStack, ports);
		String message = applyV(fnGetString, ports);

		// get render buffer
		Minecraft mcClient = Minecraft.getInstance();
		MultiBufferSource.BufferSource buffer = mcClient.renderBuffers().bufferSource();

		// get rendering engine
		EntityRenderDispatcher renderManager = mcClient.getEntityRenderDispatcher();
		Font fontRenderer = mcClient.font;

		// push matrix
		matrixStack.pushPose();

		// setup matrix
		matrixStack.scale(TEXT_SCALE_2, TEXT_SCALE_2, TEXT_SCALE_2);
		matrixStack.mulPose(renderManager.cameraOrientation());
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
		double zTranslation = TEXT_Z_TRANSLATION + oscillate(0, oscillateMax);
		matrixStack.translate(0, 0, zTranslation);

		// render message
		Matrix4f positionMatrix = matrixStack.last().pose();
		fontRenderer.drawInBatch(message, x, y, textColor, DROP_SHADOW, positionMatrix, buffer, IS_TRANSPARENT,
				TEXT_EFFECT, PACKED_LIGHT);

		// restore matrix
		matrixStack.popPose();
	}

}
