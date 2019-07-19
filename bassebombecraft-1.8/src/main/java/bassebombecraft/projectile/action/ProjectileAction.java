package bassebombecraft.projectile.action;

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Interface for project action.
 */
public interface ProjectileAction {

	/**
	 * Action when projectile hits a block or entity.
	 * 
	 * @param projectile
	 *            project tile object.
	 * @param world
	 *            world object.
	 * @param movObjPos
	 *            position of hit object.
	 */
	void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos);
}
