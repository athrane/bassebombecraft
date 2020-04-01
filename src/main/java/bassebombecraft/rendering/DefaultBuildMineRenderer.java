package bassebombecraft.rendering;

import static bassebombecraft.ModClientConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.ModConstants.BUILDMINEBOOK__TEXT_COLOR;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;

import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering construction of
 * mine in the {@linkplain BuildMineBook}.
 */
public class DefaultBuildMineRenderer implements EntityRenderer {

	/**
	 * Billboard Y-displacement.
	 */
	static final float BILLBOARD_Y_DISP = -1.5F;

	/**
	 * Renderer for rendering a bounding box as a solid .
	 */
	static final BoundingBoxRenderer aabbRenderer = new HitByRayTraceBoundingBoxRenderer();

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

		// render bounding box for block
		aabbRenderer.render(aabb, info);

		// get direction
		Direction direction = blockResult.getFace();

		// render billboard
		Vec3d aabbCenter = aabb.getCenter();
		Vec3d aabb2 = aabbCenter.add(0.0F, BILLBOARD_Y_DISP, 0);

		switch (direction) {
		case UP: {
			String message = "> Click on a GROUND block to excavate ENTRACE";
			renderTextBillboard(playerPos, aabb2, message, TEXT_BILLBOARD_ROTATION, BUILDMINEBOOK__TEXT_COLOR);
			break;
		}

		case DOWN:
			break;

		case EAST:
		case NORTH:
		case SOUTH:
		case WEST: {
			String message = "> Click on a WALL block to excavate ROOM";
			renderTextBillboard(playerPos, aabb2, message, TEXT_BILLBOARD_ROTATION, BUILDMINEBOOK__TEXT_COLOR);
			break;
		}

		default: // NO-OP
		}

	}

}
