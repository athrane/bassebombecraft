package bassebombecraft.projectile.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which hits a mob with a horizontal force.
 * 
 * If a block is hit then NO-OP.
 */
public class EmitHorizontalForce implements ProjectileAction {

	static final int FORCE = 10; // Emit force

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {

		// NO-OP if no entity was hit
		if (movObjPos.entityHit == null) {
			// NO-OP
			return;
		}

		// push mob
		Vec3d motionVec = new Vec3d(projectile.motionX, projectile.motionY, projectile.motionZ);
		double x = motionVec.x * FORCE;
		double y = motionVec.y * FORCE;
		double z = motionVec.z * FORCE;
		Vec3d motionVecForced = new Vec3d(x, y, z);
		Entity entityHit = movObjPos.entityHit;
		entityHit.move(MoverType.SELF, motionVecForced.x, motionVecForced.y, motionVecForced.z);
	}
}
