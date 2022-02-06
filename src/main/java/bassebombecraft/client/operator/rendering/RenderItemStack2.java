package bassebombecraft.client.operator.rendering;

import static bassebombecraft.client.operator.ClientOperators2.clientApplyV;
import static bassebombecraft.client.operator.DefaultClientPorts.getFnMaxtrixStack1;
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
	 * oscillate max value.
	 */
	float oscillateMax;

	/**
	 * Function to get matrix stack.
	 */
	Function<ClientPorts, MatrixStack> fnGetMatrixStack;

	/**
	 * Function to get item stack.
	 */
	Function<Ports, ItemStack> fnGetItemStack;

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
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * @param fnGetString function to get item stack.
	 * @param x           x coordinate for placement of icon.
	 * @param y           y coordinate for placement of icon.
	 */
	public RenderItemStack2(Function<Ports, ItemStack> fnGetItemStack, int x, int y) {
		this(fnGetItemStack, x, y, 0);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with matrix stack #1 from ports.
	 * 
	 * @param fnGetString  function to get item stack.
	 * @param x            x coordinate for placement of billboard.
	 * @param y            y coordinate for placement of billboard.
	 * @param oscillateMax oscillate max value
	 */
	public RenderItemStack2(Function<Ports, ItemStack> fnGetItemStack, int x, int y, float oscillateMax) {
		this.fnGetItemStack = fnGetItemStack;
		this.fnGetMatrixStack = getFnMaxtrixStack1();
		this.x = x;
		this.y = y;
		this.oscillateMax = oscillateMax;
	}

	@Override
	public void run(Ports ports) {
		MatrixStack matrixStack = clientApplyV(fnGetMatrixStack, ports);
		ItemStack itemStack = applyV(fnGetItemStack, ports);

		// get render buffer
		Minecraft mcClient = Minecraft.getInstance();

		// get rendering engine
		ItemRenderer itemRenderer = mcClient.getItemRenderer();

		// push matrix
		matrixStack.push();

		// enable lightning
		RenderHelper.enableStandardItemLighting();

		// render item stack
		itemRenderer.renderItemIntoGUI(itemStack, x, y);

		// disable lightning
		RenderHelper.disableStandardItemLighting();

		// restore matrix
		matrixStack.pop();
	}

}
