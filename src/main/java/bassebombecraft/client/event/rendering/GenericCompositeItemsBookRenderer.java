package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemHeldInMainHandOfTypeGenericCompositeItemsBook;
import static bassebombecraft.util.function.Functions.getFnCastToGenericCompositeItemsBook;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.CalculateRightBottomTextAnchor;
import bassebombecraft.client.operator.rendering.RenderItemStack2;
import bassebombecraft.client.operator.rendering.RenderOverlayText2;
import bassebombecraft.item.book.GenericCompositeItemsBook;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Rendering the composite items in {@linkplain GenericCompositeItemsBook} sub
 * classes.
 */
public class GenericCompositeItemsBookRenderer {

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
	static final int X_POS = -20;

	/**
	 * Item stack x displacement.
	 */
	static final int X_DISP = 16;

	/**
	 * Item stack y position.
	 */
	static final int Y_POS = -20;

	/**
	 * Rendering ports.
	 */
	static ClientPorts ports = getInstance();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {

		Function<Ports, GenericCompositeItemsBook> fnGetBook = p -> getFnCastToGenericCompositeItemsBook()
				.apply(p.getItemStack1().getItem());
		Function<Ports, ItemStack> fnGetItemStack0 = p -> fnGetBook.apply(p).getCompositeItems()[0];
		Function<Ports, ItemStack> fnGetItemStack1 = p -> fnGetBook.apply(p).getCompositeItems()[1];
		Function<Ports, ItemStack> fnGetItemStack2 = p -> fnGetBook.apply(p).getCompositeItems()[2];
		Function<Ports, ItemStack> fnGetItemStack3 = p -> fnGetBook.apply(p).getCompositeItems()[3];

		return new Sequence2(new CalculateRightBottomTextAnchor(getFnGetString1()),
				new RenderOverlayText2(getFnGetString1(), TEXT_X_POS, TEXT_Y_POS),
				new RenderItemStack2(fnGetItemStack0, X_POS + (0 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack1, X_POS + (1 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack2, X_POS + (2 * X_DISP), Y_POS),
				new RenderItemStack2(fnGetItemStack3, X_POS + (3 * X_DISP), Y_POS));
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

			// exit if item in main hand isn't a sub class of GenericRightClickedBook2
			if (!isItemHeldInMainHandOfTypeGenericCompositeItemsBook(player))
				return;

			// Render active configuration
			ports.setRenderGameOverlayEvent1(event);
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
		return "Combination:";
	}

}
