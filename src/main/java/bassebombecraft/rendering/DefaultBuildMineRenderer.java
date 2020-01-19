package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.RAYTRACE_FLUIDS;
import static bassebombecraft.ModConstants.RAYTRACE_OUTLINE;
import static bassebombecraft.ModConstants.RAYTRACE_RANGE;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderDebugBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;

import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Renderer} for rendering construction of
 * mine in the {@linkplain BuildMineBook}.
 */
public class DefaultBuildMineRenderer implements EntityRenderer {

	/**
	 * Renderer for rendering a bounding box as a solid .
	 */
	static final BoundingBoxRenderer aabbRenderer = new SolidBoundingBoxRenderer();

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// type cast
		PlayerEntity player = (PlayerEntity) entity;

		// get ray trace result
		RayTraceResult result = info.getResult();
		
		// exit if ray trace result is defined
		if (info.getResult() == null)
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

		// render bounding box for block
		aabbRenderer.render(aabb, info);

		// render billboard
		Vec3d aabbCenter = aabb.getCenter();
		renderDebugBillboard(playerPos, aabbCenter);

		String message = "Book of Enterpise: Click on block to generate mine..";
		renderTextBillboard(playerPos, aabbCenter.add(0.0F, 0.0F, 0), message, TEXT_BILLBOARD_ROTATION);
	}

}
