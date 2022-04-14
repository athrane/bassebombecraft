package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.operator.DefaultPorts.getFnGetItemStack1;
import static bassebombecraft.operator.Operators2.run;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.RenderItemStack2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Renders item in main hand.
 */
public class DebugRenderer {

	/**
	 * Icon x position.
	 */
	static final int X_POS = 0;

	/**
	 * Icon y position.
	 */
	static final int Y_POS = 0;

	/**
	 * Icon oscillation value.
	 */
	static final float OSCILLIATION = 5F;

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new RenderItemStack2(getFnGetItemStack1(), X_POS, Y_POS),
				new RenderItemStack2(getFnGetItemStack1(), X_POS + 10, Y_POS + 10),
				new RenderItemStack2(getFnGetItemStack1(), X_POS + 20, Y_POS + 20),
				new RenderItemStack2(getFnGetItemStack1(), X_POS + 30, Y_POS + 30),
				new RenderItemStack2(getFnGetItemStack1(), X_POS + 40, Y_POS + 40));
	};

	/**
	 * Handle {@linkplain RenderWorldLastEvent}.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void handleRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
		try {

			// exit if not hotbar element getting rendered.
			if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
				return;

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// get item in main hand
			ItemStack heldItemStack = player.getHeldItemMainhand();
			if (heldItemStack == null)
				return;

			render(event.getMatrixStack(), heldItemStack);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render help text.
	 * 
	 * @param matrixStack   matrix stack for rendering transforms.
	 * @param heldItemStack held item stack.
	 */
	static void render(MatrixStack matrixStack, ItemStack heldItemStack) {

		// setup operator and execute
		ClientPorts ports = getInstance();
		ports.setMatrixStack1(matrixStack);
		ports.setItemStack1(heldItemStack);
		run(ports, splOp.get());
	}

}
