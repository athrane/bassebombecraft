package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.HUD_TEXT_COLOR;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.DefaultPorts.getFnGetString2;
import static bassebombecraft.operator.DefaultPorts.getFnGetStrings1;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.mojang.blaze3d.vertex.PoseStack;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.RenderMultiLineTextBillboard2;
import bassebombecraft.client.operator.rendering.RenderTextBillboard2;
import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.operator.Operator2;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Rendering charmed information in the {@linkplain HudItem}.
 */
public class HudItemCharmedInfoRenderer {

	/**
	 * Function to create charmed mob name
	 */
	static Function<CharmedMob, String> fnCreateMessage = c -> c.getName() + ", Charm duration: " + c.getDuration();

	/**
	 * HUD text x position.
	 */
	static final int TEXT_X_POS = 110;

	/**
	 * HUD text y position.
	 */
	static final int TEXT_Y_POS = 10;

	/**
	 * Text oscillation value.
	 */
	static final float TEXT_OSCILLIATION = 5F;

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Operator2[] ops = {
				new RenderTextBillboard2(getFnGetString1(), TEXT_X_POS, TEXT_Y_POS, TEXT_OSCILLIATION, HUD_TEXT_COLOR),
				new RenderTextBillboard2(getFnGetString2(), TEXT_X_POS, TEXT_Y_POS + 10, TEXT_OSCILLIATION,
						HUD_TEXT_COLOR),
				new RenderMultiLineTextBillboard2(getFnGetStrings1(), TEXT_X_POS, TEXT_Y_POS + 20, TEXT_OSCILLIATION,
						HUD_TEXT_COLOR) };
		return ops;
	};

	/**
	 * Handle {@linkplain RenderWorldLastEvent}.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			Player player = getClientSidePlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD.get()))
				return;

			render(event.getMatrixStack(), player);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render charmed info.
	 * 
	 * @param matrixStack matrix static for rendering transforms.
	 * @param player      player object.
	 */
	static void render(PoseStack matrixStack, Player player) {

		// get charmed entities
		CharmedMobsRepository repository = getProxy().getClientCharmedMobsRepository();
		Stream<CharmedMob> charmedMobs = repository.get();
		int charmedSize = repository.size();

		// create list of members
		Stream<String> messages2 = charmedMobs.limit(TEAM_MEMBERS_TO_RENDER).map(fnCreateMessage);

		// setup operator and execute
		ClientPorts ports = getInstance();
		ports.setMatrixStack1(matrixStack);
		ports.setString1("CHARMED MOBS");
		ports.setString2("Number charmed: " + charmedSize);
		ports.setStrings1(messages2);
		run(ports, splOp.get());

	}

}
