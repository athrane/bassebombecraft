package bassebombecraft.projectile.action;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Interface for projectile action.
 */
@Deprecated
public interface ProjectileAction {

	/**
	 * Action when projectile hits a block or entity.
	 * 
	 * @param projectile project tile object.
	 * @param world      world object.
	 * @param result     position of hit object.
	 */
	void execute(ThrowableProjectile projectile, Level world, HitResult result);
}
