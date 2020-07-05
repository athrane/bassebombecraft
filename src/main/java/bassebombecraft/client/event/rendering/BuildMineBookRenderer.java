package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ClientModConstants.BUILDMINEBOOK_TEXT_COLOR;
import static bassebombecraft.ClientModConstants.HUD_LINE_COLOR;
import static bassebombecraft.ModConstants.BUILD_MINE_BOOK;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.rendertype.OverlayLines.OVERLAY_LINES;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemHeldInEitherHands;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;

import java.util.function.Supplier;

import bassebombecraft.client.op.rendering.RenderTextBillboard2;
import bassebombecraft.client.op.rendering.RenderWireframeBoundingBox2;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;

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
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Operator2[] ops = { new RenderWireframeBoundingBox2(AABB_OSCILLIATION, HUD_LINE_COLOR, OVERLAY_LINES),
				new RenderTextBillboard2(getFnGetString1(), -5, -20, TEXT_OSCILLIATION, BUILDMINEBOOK_TEXT_COLOR) };
		return ops;
	};

	public static void handleHighlightBlockEvent(HighlightBlock event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// render if build mine book is in hand
			if (!isItemHeldInEitherHands(player, BUILD_MINE_BOOK))
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
	static void render(HighlightBlock event, PlayerEntity player) {

		// get ray trace result
		BlockRayTraceResult result = event.getTarget();

		// exit if player isn't looking at a block
		if (result.getType() != BLOCK)
			return;

		// create aabb for block
		BlockPos blockPos = result.getPos();
		AxisAlignedBB aabb = new AxisAlignedBB(blockPos);

		// get direction
		Direction direction = result.getFace();

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
		Ports ports = getInstance();
		ports.setAabb(aabb);
		ports.setMatrixStack(event.getMatrix());
		ports.setString1(message);
		run(ports, splOp.get());
	}

}
