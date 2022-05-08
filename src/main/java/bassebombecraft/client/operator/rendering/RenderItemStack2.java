package bassebombecraft.client.operator.rendering;

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
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
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
	Function<Ports, Vec2> fnGetTextAnchor;

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
			Function<ClientPorts, RenderGameOverlayEvent> fnGetEvent, Function<Ports, Vec2> fnGetTextAnchor, int x,
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
		Vec2 textAnchor = applyV(fnGetTextAnchor, ports);
		ItemStack itemStack = applyV(fnGetItemStack, ports);

		// get rendering engine
		Minecraft mcClient = Minecraft.getInstance();
		ItemRenderer itemRenderer = mcClient.getItemRenderer();

		// push matrix
		PoseStack matrixStack = event.getMatrixStack();
		matrixStack.pushPose();

		// enable lightning
		// Lighting.turnBackOn(); // TODO: VERIFY and delete this

		// calculate icon position
		int xp = (int) (textAnchor.x + x);
		int yp = (int) (textAnchor.y + y);

		// render item stack
		itemRenderer.renderGuiItem(itemStack, xp, yp);

		// disable lightning
		//Lighting.turnOff(); // TODO: VERIFY and delete this

		// restore matrix
		matrixStack.popPose();
	}

}
