package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.item.RegisteredItems.COMPOSITE;
import static bassebombecraft.item.composite.CompositeMagicItem.getItemStackHandler;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemHeldInMainHand;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.CalculateRightBottomTextAnchor;
import bassebombecraft.client.operator.rendering.RenderItemStack2;
import bassebombecraft.client.operator.rendering.RenderOverlayText2;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.composite.CompositeMagicItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Rendering of help information and current configuration in the
 * {@linkplain CompositeMagicItem}.
 */
public class CompositeMagicItemRenderer {

	/**
	 * Duration ID.
	 */
	static final String CONFIG_DURATION_ID = UUID.randomUUID().toString();

	/**
	 * Duration ID #2.
	 */
	static final String HELP_DURARION_ID = UUID.randomUUID().toString();

	/**
	 * HUD text x position.
	 */
	static final int TEXT_X_POS = -20;

	/**
	 * HUD text y position.
	 */
	static final int TEXT_Y_POS = -30;

	/**
	 * Item stack x position.
	 */
	static final int X_POS = 20;

	/**
	 * Item stack x displacement.
	 */
	static final int X_DISP = 16;

	/**
	 * Item stack y position.
	 */
	static final int Y_POS = 220;

	/**
	 * Rendering ports.
	 */
	static ClientPorts ports = getInstance();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, ItemStack> fnGetItemStack0 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(0);
		Function<Ports, ItemStack> fnGetItemStack1 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(1);
		Function<Ports, ItemStack> fnGetItemStack2 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(2);
		Function<Ports, ItemStack> fnGetItemStack3 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(3);
		Function<Ports, ItemStack> fnGetItemStack4 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(3);
		Function<Ports, ItemStack> fnGetItemStack5 = p -> getItemStackHandler(p.getItemStack1()).getStackInSlot(3);

		return new Sequence2(
				new CalculateRightBottomTextAnchor(getFnGetString1()),
				new RenderOverlayText2(getFnGetString1(), TEXT_X_POS, TEXT_Y_POS),
				new RenderItemStack2(fnGetItemStack0, X_POS + (0 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack1, X_POS + (1 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack2, X_POS + (2 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack3, X_POS + (3 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack4, X_POS + (4 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack5, X_POS + (5 * X_DISP), Y_POS));
	};

	/**
	 * Handle {@linkplain RenderGameOverlayEvent}.
	 * 
	 * @param event event to trigger rendering of the active configuration.
	 */
	public static void handleRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// exit if composite item isn't in main hand
			if (!isItemHeldInMainHand(player, COMPOSITE.get()))
				return;

			// Render active configuration
			ports.setRenderGameOverlayEvent1(event);
			ports.setMatrixStack1(event.getMatrixStack());
			ports.setString1(getGuiString());
			ports.setItemStack1(player.getHeldItemMainhand());
			run(ports, splOp.get());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Return GUI text string.
	 * 
	 * @return text string
	 */
	static String getGuiString() {
		double value = GeometryUtils.oscillate(0, 2);
		if (value > 1)
			return "Shift+Click to edit";
		return     "Active combination:";
	}

}
