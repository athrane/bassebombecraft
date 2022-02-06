package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnRenderGameOverlayEvent1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which renders overlay
 * text.
 * 
 * Supports rendering of overlay text in the renderer instances processing the
 * {@linkplain RenderGameOverlayEvent}.
 */
public class RenderOverlayText2 implements Operator2 {

	/**
	 * Function get message to render.
	 */
	Function<Ports, String> fnGetString;

	/**
	 * Function to get matrix stack.
	 */
	Function<ClientPorts, MatrixStack> fnGetMatrixStack;

	/**
	 * Function to get {@linkplain RenderGameOverlayEvent}.
	 */
	Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent;

	/**
	 * X coordinate for placement of text.
	 */
	float x;

	/**
	 * Y coordinate for placement of text.
	 */
	float y;

	/**
	 * Constructor.
	 * 
	 * @param fnGetString function to get message.
	 * @param x           x coordinate for placement of text.
	 * @param y           y coordinate for placement of text.
	 */
	public RenderOverlayText2(Function<Ports, String> fnGetString, float x, float y) {
		this(fnGetString, getFnRenderGameOverlayEvent1(), x, y);
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetString function to get message.
	 * @param fnGetEvent  function to {@linkplain RenderGameOverlayEvent}
	 * @param x           x coordinate for placement of text.
	 * @param y           y coordinate for placement of text.
	 */
	public RenderOverlayText2(Function<Ports, String> fnGetString,
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent, float x, float y) {
		this.fnGetString = fnGetString;
		this.fnGetEvent = fnGetEvent;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run(Ports ports) {
		RenderGameOverlayEvent event = clientApplyV(fnGetEvent, ports);
		String message = applyV(fnGetString, ports);

		// get rendering engine
		Minecraft mcClient = Minecraft.getInstance();
		EntityRendererManager renderManager = mcClient.getRenderManager();
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		// push matrix
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();

		// render text
		fontRenderer.drawString(matrixStack, message, x, y, TEXT_COLOR);

		// restore matrix
		matrixStack.pop();
	}

}
