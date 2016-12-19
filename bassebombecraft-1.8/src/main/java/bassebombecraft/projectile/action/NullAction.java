package bassebombecraft.projectile.action;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which implements NO-OP action.
 */
public class NullAction implements ProjectileAction{

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {		
		// NO-OP
	}

}
