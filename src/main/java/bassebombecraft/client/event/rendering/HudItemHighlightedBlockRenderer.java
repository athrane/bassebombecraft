package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ClientModConstants.HUD_LINE_COLOR;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.client.rendering.rendertype.RenderTypes.OVERLAY_LINES;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;
import static bassebombecraft.operator.DefaultPorts.getFnGetString2;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;

import java.util.function.Supplier;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.RenderTextBillboard2;
import bassebombecraft.client.operator.rendering.RenderWireframeBoundingBox2;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;

/**
 * Client side renderer for rendering the highlighted block in the
 * {@linkplain HudItem}.
 */
public class HudItemHighlightedBlockRenderer {

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
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new RenderWireframeBoundingBox2(AABB_OSCILLIATION, HUD_LINE_COLOR, OVERLAY_LINES),
				new RenderTextBillboard2(getFnGetString1(), -5, 0, TEXT_OSCILLIATION),
				new RenderTextBillboard2(getFnGetString2(), -5, -10, TEXT_OSCILLIATION));
	};

	public static void handleHighlightBlockEvent(HighlightBlock event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD.get()))
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

		// get world
		World world = player.getEntityWorld();

		// Get block type
		BlockState blockstate = world.getBlockState(blockPos);
		String message = blockstate.getBlock().getTranslatedName().getUnformattedComponentText();

		// get aabb center
		Vector3d aabbCenter = aabb.getCenter();

		// setup operator and execute
		ClientPorts ports = getInstance();
		ports.setAabb1(aabb);
		ports.setMatrixStack1(event.getMatrix());
		ports.setString1(message);
		ports.setString2(aabbCenter.toString());
		run(ports, splOp.get());

	}

}
