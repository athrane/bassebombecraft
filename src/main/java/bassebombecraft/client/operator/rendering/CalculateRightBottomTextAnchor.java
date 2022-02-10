package bassebombecraft.client.operator.rendering;

import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnRenderGameOverlayEvent1;
import static bassebombecraft.operator.DefaultPorts.getBcSetVector2f1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which calculates the
 * text position at right bottom of the screen based on the screen resolution
 * and the length of the text.
 * 
 * Supports calculation of the position in the renderer instances processing the
 * {@linkplain RenderGameOverlayEvent}.
 * 
 * The text anchor is stored as a {@linkplain Vector2f} in the ports.
 */
public class CalculateRightBottomTextAnchor implements Operator2 {

	/**
	 * Function get message to render.
	 */
	Function<Ports, String> fnGetString;

	/**
	 * Function to get {@linkplain RenderGameOverlayEvent}.
	 */
	Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent;

	/**
	 * Function to set text anchor.
	 */
	BiConsumer<Ports, Vector2f> bcSetPos;

	/**
	 * Constructor.
	 * 
	 * Instance is configured to set vector2f #1 as calculated text anchor from
	 * ports.
	 * 
	 * @param fnGetString function to get message.
	 */
	public CalculateRightBottomTextAnchor(Function<Ports, String> fnGetString) {
		this(fnGetString, getFnRenderGameOverlayEvent1());
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured to set vector2f #1 as calculated text anchor from
	 * ports.
	 * 
	 * @param fnGetString function to get message.
	 * @param fnGetEvent  function to {@linkplain RenderGameOverlayEvent}
	 */
	public CalculateRightBottomTextAnchor(Function<Ports, String> fnGetString,
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent) {
		this(fnGetString, fnGetEvent, getBcSetVector2f1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetString function to get message.
	 * @param fnGetEvent  function to {@linkplain RenderGameOverlayEvent}
	 * @param bcSetPos    function to set calculated text anchor.
	 */
	public CalculateRightBottomTextAnchor(Function<Ports, String> fnGetString,
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent, BiConsumer<Ports, Vector2f> bcSetPos) {
		this.fnGetString = fnGetString;
		this.fnGetEvent = fnGetEvent;
		this.bcSetPos = bcSetPos;
	}

	@Override
	public void run(Ports ports) {
		RenderGameOverlayEvent event = clientApplyV(fnGetEvent, ports);
		String message = applyV(fnGetString, ports);

		// Get window resolution
		MainWindow mainWindow = event.getWindow();
		int height = mainWindow.getScaledHeight();
		int width = mainWindow.getScaledWidth();

		// get rendering engine
		Minecraft mcClient = Minecraft.getInstance();
		EntityRendererManager renderManager = mcClient.getRenderManager();
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		// get text length
		int textWidth = fontRenderer.getStringWidth(message);

		bcSetPos.accept(ports, new Vector2f(width - textWidth, height));
	}

}
