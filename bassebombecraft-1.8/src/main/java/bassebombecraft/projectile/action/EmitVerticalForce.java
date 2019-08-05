package bassebombecraft.projectile.action;

import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which hits a mob with a
 * vertical force.
 * 
 * If a block is hit then NO-OP.
 */
public class EmitVerticalForce implements ProjectileAction {

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
		
		// push mob up
		Vec3d motionVec = new Vec3d(0, FORCE, 0);		
		entity.move(MoverType.SELF, motionVec);
	}

}
