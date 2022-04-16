package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnRenderGameOverlayEvent1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVector2f1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec2;
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
	 * Function to get {@linkplain RenderGameOverlayEvent}.
	 */
	Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent;

	/**
	 * Function to get text anchor.
	 */
	Function<Ports, Vec2> fnGetTextAnchor;

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
	 * Instance is configured to get {@linkplain RenderGameOverlayEvent} from client
	 * ports. Instance is configured to get vector2f #1 as text anchor from ports.
	 * 
	 * @param fnGetString function to get message.
	 * @param x           x coordinate for placement of text.
	 * @param y           y coordinate for placement of text.
	 */
	public RenderOverlayText2(Function<Ports, String> fnGetString, float x, float y) {
		this(fnGetString, getFnRenderGameOverlayEvent1(), getFnGetVector2f1(), x, y);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured to get {@linkplain RenderGameOverlayEvent} from ports.
	 * 
	 * @param fnGetString     function to get message.
	 * @param fnGetEvent      function to get {@linkplain RenderGameOverlayEvent}.
	 * @param fnGetTextAnchor function to get text anchor .
	 * @param x               x coordinate for placement of text.
	 * @param y               y coordinate for placement of text.
	 */
	public RenderOverlayText2(Function<Ports, String> fnGetString,
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent, Function<Ports, Vec2> fnGetTextAnchor,
			float x, float y) {
		this.fnGetString = fnGetString;
		this.fnGetEvent = fnGetEvent;
		this.fnGetTextAnchor = fnGetTextAnchor;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run(Ports ports) {
		String message = applyV(fnGetString, ports);
		RenderGameOverlayEvent event = clientApplyV(fnGetEvent, ports);
		Vec2 textAnchor = applyV(fnGetTextAnchor, ports);

		// get rendering engine
		Minecraft mcClient = Minecraft.getInstance();
		EntityRenderDispatcher renderManager = mcClient.getEntityRenderDispatcher();
		Font fontRenderer = renderManager.getFont();

		// push matrix
		PoseStack matrixStack = event.getMatrixStack();
		matrixStack.pushPose();

		// calculate text position
		float xp = textAnchor.x + x;
		float yp = textAnchor.y + y;

		// render text
		fontRenderer.draw(matrixStack, message, xp, yp, TEXT_COLOR);

		// restore matrix
		matrixStack.popPose();
	}

}
