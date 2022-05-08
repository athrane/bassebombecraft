package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ClientModConstants.BUILDMINEBOOK_TEXT_COLOR;
import static bassebombecraft.ClientModConstants.HUD_LINE_COLOR;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.OVERLAY_LINES;
import static bassebombecraft.item.RegisteredItems.MINE_BOOK;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemHeldInEitherHands;
import static net.minecraft.world.phys.HitResult.Type.BLOCK;

import java.util.function.Supplier;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.RenderTextBillboard2;
import bassebombecraft.client.operator.rendering.RenderWireframeBoundingBox2;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.DrawSelectionEvent.HighlightBlock;

/**
 * Client side renderer for rendering construction of mine in the
 * {@linkplain BuildMineBook}.
 */
public class BuildMineBookRenderer {

	/**
	 * Bounding box oscillation value.
	 */
	static final float AABB_OSCILLIATION = 0.025F;

	/**
	 * Highlighted text oscillation value.
	 */
	static final float TEXT_OSCILLIATION = 25F;

	/**
	 * Rendering ports.
	 */
	static ClientPorts ports = getInstance();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new RenderWireframeBoundingBox2(AABB_OSCILLIATION, HUD_LINE_COLOR, OVERLAY_LINES),
				new RenderTextBillboard2(getFnGetString1(), -5, -20, TEXT_OSCILLIATION, BUILDMINEBOOK_TEXT_COLOR));
	};

	public static void handleHighlightBlockEvent(HighlightBlock event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			Player player = getClientSidePlayer();

			// render if build mine book is in hand
			if (!isItemHeldInEitherHands(player, MINE_BOOK.get()))
				return;
			render(event, player);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}

	/**
	 * Render charmed info.
	 * 
	 * @param event  the highlight block event.
	 * @param player player object.
	 */
	static void render(HighlightBlock event, Player player) {

		// get ray trace result
		BlockHitResult result = event.getTarget();

		// exit if player isn't looking at a block
		if (result.getType() != BLOCK)
			return;

		// create aabb for block
		BlockPos blockPos = result.getBlockPos();
		AABB aabb = new AABB(blockPos);

		// get direction
		Direction direction = result.getDirection();

		// generate message
		String message = "N/A";
		switch (direction) {
		case UP: {
			message = "> Click on a GROUND block to excavate ENTRACE";
			break;
		}

		case DOWN:
			break;

		case EAST:
		case NORTH:
		case SOUTH:
		case WEST: {
			message = "> Click on a WALL block to excavate ROOM";
			break;
		}

		default: // NO-OP
		}

		// setup operator and execute
		ports.setAabb1(aabb);
		ports.setMatrixStack1(event.getMatrix());
		ports.setString1(message);
		run(ports, splOp.get());
	}

}
