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
import static net.minecraft.world.phys.HitResult.Type.BLOCK;

import java.util.function.Supplier;

import bassebombecraft.client.operator.ClientPorts;
import bassebombecraft.client.operator.rendering.RenderTextBillboard2;
import bassebombecraft.client.operator.rendering.RenderWireframeBoundingBox2;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
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
			Player player = getClientSidePlayer();

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
	static void render(HighlightBlock event, Player player) {

		// get ray trace result
		BlockHitResult result = event.getTarget();

		// exit if player isn't looking at a block
		if (result.getType() != BLOCK)
			return;

		// create aabb for block
		BlockPos blockPos = result.getBlockPos();
		AABB aabb = new AABB(blockPos);

		// get world
		Level world = player.getCommandSenderWorld();

		// Get block type
		BlockState blockstate = world.getBlockState(blockPos);
		Block block = blockstate.getBlock();
		String message = I18n.get(block.getDescriptionId());

		// get aabb center
		Vec3 aabbCenter = aabb.getCenter();

		// setup operator and execute
		ClientPorts ports = getInstance();
		ports.setAabb1(aabb);
		ports.setMatrixStack1(event.getMatrix());
		ports.setString1(message);
		ports.setString2(aabbCenter.toString());
		run(ports, splOp.get());

	}

}
