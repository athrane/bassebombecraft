package bassebombecraft.projectile.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
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
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// NO-OP if no entity was hit
		if (movObjPos.entityHit == null) {
			// NO-OP
			return;
		}

		// push mob up
		Entity entityHit = movObjPos.entityHit;
		entityHit.move(MoverType.SELF,0, FORCE, 0);
	}

}
