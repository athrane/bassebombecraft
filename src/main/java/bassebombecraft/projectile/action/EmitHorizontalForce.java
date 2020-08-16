package bassebombecraft.projectile.action;

import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.EntityRayTraceResult;
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
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if no entity was hit
		if (!isEntityHit(result))
			return;
		
		// exit if result isn't entity ray trace result;
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();
		
		// push mob
		Vec3d motion = projectile.getMotion();
		Vec3d motionVec = new Vec3d(motion.getX(), motion.getY(), motion.getZ());
		double x = motionVec.x * FORCE;
		double y = motionVec.y * FORCE;
		double z = motionVec.z * FORCE;
		Vec3d motionVecForced = new Vec3d(x, y, z);				
		entity.move(MoverType.SELF, motionVecForced);
	}
}
