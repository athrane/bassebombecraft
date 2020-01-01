package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderDebugBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;

import bassebombecraft.player.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Renderer} for rendering the block looked at
 * by the player.
 */
public class DefaultLookAtBlockRenderer implements EntityRenderer {

	/**
	 * Ray trace range in blocks.
	 */
	public static final double RANGE = 20;

	/**
	 * Ray trace mode for fluids.
	 */
	public static final FluidMode TRACE_FLUIDS = RayTraceContext.FluidMode.ANY;

	/**
	 * Ray trace mode for blocks.
	 */
	public static final BlockMode TRACE_OUTLINE = RayTraceContext.BlockMode.OUTLINE;

	/**
	 * Renderer for rendering a bounding box with a wire frame.
	 */
	static final BoundingBoxRenderer aabbWireframeRenderer = new WireframeBoundingBoxRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// type cast
		PlayerEntity player = (PlayerEntity) entity;

		// get world
		World world = player.world;

		// ray trace blocks
		Vec3d look = player.getLookVec();
		Vec3d startPos = player.getPositionVec();
		startPos = startPos.add(0, player.getEyeHeight(), 0);
		Vec3d endPos = startPos.add(look.mul(RANGE, RANGE, RANGE));
		RayTraceContext context = new RayTraceContext(startPos, endPos, TRACE_OUTLINE, TRACE_FLUIDS, player);
		BlockRayTraceResult result = world.rayTraceBlocks(context);

		// exit if player isn't looking at a block
		if (result.getType() != Type.BLOCK)
			return;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());
		
		// create aabb for block
		BlockPos blockPos = result.getPos();
		AxisAlignedBB aabb = new AxisAlignedBB(blockPos);

		// Get block type
		BlockState blockstate = world.getBlockState(blockPos);
		String message = blockstate.getBlock().getNameTextComponent().getUnformattedComponentText();

		// render bounding box for block
		aabbWireframeRenderer.render(aabb, info);

		// render billboard
		Vec3d aabbCenter = aabb.getCenter();
		renderDebugBillboard(playerPos, aabbCenter);
		renderTextBillboard(playerPos, aabbCenter.add(0.0F, -2.0F, 0), message, TEXT_BILLBOARD_ROTATION);

	}

}
