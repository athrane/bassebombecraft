package bassebombecraft.projectile.action;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which implements NO-OP action.
 */
public class NullAction implements ProjectileAction{

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {		
		// NO-OP
	}

}
