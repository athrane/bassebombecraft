package bassebombecraft.client.operator.rendering;

import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnRenderGameOverlayEvent1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVector2f1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which renders an item
 * stack icon.
 * 
 * Supports rendering of item stack icon in the renderer instances handling
 * processing the {@linkplain RenderGameOverlayEvent}.
 */
public class RenderItemStack2 implements Operator2 {

	/**
	 * Function to get item stack.
	 */
	Function<Ports, ItemStack> fnGetItemStack;

	/**
	 * Function to get {@linkplain RenderGameOverlayEvent}.
	 */
	Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent;

	/**
	 * Function to get text anchor.
	 */
	Function<Ports, Vector2f> fnGetTextAnchor;

	/**
	 * X coordinate for placement of icon.
	 */
	int x;

	/**
	 * Y coordinate for placement of icon.
	 */
	int y;

	/**
	 * Constructor.
	 * 
	 * Instance is configured to get {@linkplain RenderGameOverlayEvent} from client
	 * ports. Instance is configured to get vector2f #1 as text anchor from ports.
	 * 
	 * @param fnGetItemStack function to get item stack.
	 * @param x              x coordinate for placement of icon.
	 * @param y              y coordinate for placement of icon.
	 */
	public RenderItemStack2(Function<Ports, ItemStack> fnGetItemStack, int x, int y) {
		this(fnGetItemStack, getFnRenderGameOverlayEvent1(), getFnGetVector2f1(), x, y);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured to get {@linkplain RenderGameOverlayEvent} from ports.
	 * 
	 * @param fnGetItemStack  function to get item stack.
	 * @param fnGetEvent      function to get {@linkplain RenderGameOverlayEvent}.
	 * @param fnGetTextAnchor function to get text anchor.
	 * @param x               x coordinate for placement of billboard.
	 * @param y               y coordinate for placement of billboard.
	 */
	public RenderItemStack2(Function<Ports, ItemStack> fnGetItemStack,
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent, Function<Ports, Vector2f> fnGetTextAnchor, int x,
			int y) {
		this.fnGetItemStack = fnGetItemStack;
		this.fnGetEvent = fnGetEvent;
		this.fnGetTextAnchor = fnGetTextAnchor;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run(Ports ports) {
		RenderGameOverlayEvent event = clientApplyV(fnGetEvent, ports);
		Vector2f textAnchor = applyV(fnGetTextAnchor, ports);
		ItemStack itemStack = applyV(fnGetItemStack, ports);

		// get rendering engine
		Minecraft mcClient = Minecraft.getInstance();
		ItemRenderer itemRenderer = mcClient.getItemRenderer();

		// push matrix
		MatrixStack matrixStack = event.getMatrixStack();
		matrixStack.push();

		// enable lightning
		RenderHelper.enableStandardItemLighting();

		// calculate icon position
		int xp = (int) (textAnchor.x + x);
		int yp = (int) (textAnchor.y + y);
		
		// render item stack
		itemRenderer.renderItemIntoGUI(itemStack, xp, yp);

		// disable lightning
		RenderHelper.disableStandardItemLighting();

		// restore matrix
		matrixStack.pop();
	}

}
