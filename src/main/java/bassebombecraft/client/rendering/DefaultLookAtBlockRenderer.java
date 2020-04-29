package bassebombecraft.client.rendering;

import static bassebombecraft.ClientModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.client.rendering.RenderingUtils.renderTextBillboard;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;

import bassebombecraft.player.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Renderer} for rendering the block looked at
 * by the player.
 */
public class DefaultLookAtBlockRenderer implements EntityRenderer {

	/**
	 * Renderer for rendering a bounding box with a wire frame.
	 */
	static final BoundingBoxRenderer aabbRenderer = new WireframeBoundingBoxRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// type cast
		PlayerEntity player = (PlayerEntity) entity;

		// get ray trace result
		RayTraceResult result = info.getResult();

		// exit if ray trace result isn't defined
		if (!info.isRayTraceResultDefined())
			return;

		// exit if player isn't looking at a block
		if (result.getType() != BLOCK)
			return;

		// type cast
		BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// create aabb for block
		BlockPos blockPos = blockResult.getPos();
		AxisAlignedBB aabb = new AxisAlignedBB(blockPos);

		// get world
		World world = player.world;

		// Get block type
		BlockState blockstate = world.getBlockState(blockPos);
		String message = blockstate.getBlock().getNameTextComponent().getUnformattedComponentText();

		// render bounding box for block
		aabbRenderer.render(aabb, info);

		// render billboard
		Vec3d aabbCenter = aabb.getCenter();
		renderTextBillboard(playerPos, aabbCenter.add(0.0F, -2.0F, 0), message, TEXT_BILLBOARD_ROTATION);

		message = aabbCenter.toString();
		renderTextBillboard(playerPos, aabbCenter.add(0.0F, -2.25F, 0), message, TEXT_BILLBOARD_ROTATION);
	}

}
