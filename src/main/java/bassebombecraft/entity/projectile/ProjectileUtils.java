package bassebombecraft.entity.projectile;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Fireball;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable;

/**
 * Utility class for projectile calculations.
 */
public class ProjectileUtils {

	/**
	 * Return true if block was hit by ray trace result.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if block was hit.
	 */
	public static boolean isBlockHit(RayTraceResult result) {
		if (result == null)
			return false;
		return (result.getType() == RayTraceResult.Type.BLOCK);
	}

	/**
	 * Return true if entity was hit by ray trace result.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if entity was hit.
	 */
	public static boolean isEntityHit(RayTraceResult result) {
		if (result == null)
			return false;
		return (result.getType() == RayTraceResult.Type.ENTITY);
	}

	/**
	 * Return true if ray trace result missed.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result was a miss.
	 */
	public static boolean isNothingHit(RayTraceResult result) {
		if (result == null)
			return false;
		return (result.getType() == RayTraceResult.Type.MISS);
	}

	/**
	 * Return true if ray trace result a {@linkplain EntityRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain EntityRayTraceResult}.
	 */
	public static boolean isTypeEntityRayTraceResult(RayTraceResult result) {
		if (result == null)
			return false;
		return result instanceof EntityRayTraceResult;
	}

	/**
	 * Return true if ray trace result a {@linkplain BlockRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain BlockRayTraceResult}.
	 */
	public static boolean isTypeBlockRayTraceResult(RayTraceResult result) {
		if (result == null)
			return false;
		return result instanceof BlockRayTraceResult;
	}

	/**
	 * Resolve invoker entity from projectile impact event
	 * {@linkplain ProjectileImpactEvent}.
	 * 
	 * @param event event to resolve projectile invoker from.
	 * 
	 * @return return the invoker entity as a {@linkplain LivingEntity}. If the
	 *         invoker can't be resolved as a living entity then a the returned
	 *         optional is empty.
	 */
	public static Optional<LivingEntity> resolveInvoker(ProjectileImpactEvent event) {

		// resolve invoker from arrow projectile
		if (event instanceof ProjectileImpactEvent.Arrow) {

			// type cast event and get invoker
			Arrow arrowEvent = (ProjectileImpactEvent.Arrow) event;
			AbstractArrowEntity arrowProjectile = arrowEvent.getArrow();
			Entity invoker = arrowProjectile.getShooter();

			// return invoker as LivingEntity
			if (invoker instanceof LivingEntity)
				return Optional.of((LivingEntity) invoker);
			else
				return Optional.empty();
		}

		// resolve invoker from fireball projectile
		if (event instanceof ProjectileImpactEvent.Fireball) {

			// type cast event and get invoker
			Fireball fireballEvent = (ProjectileImpactEvent.Fireball) event;
			DamagingProjectileEntity fireballProjectile = fireballEvent.getFireball();
			return Optional.of(fireballProjectile.shootingEntity);
		}

		// resolve invoker from throwable projectile
		if (event instanceof ProjectileImpactEvent.Throwable) {

			// type cast event and get invoker
			Throwable throwableEvent = (ProjectileImpactEvent.Throwable) event;
			ThrowableEntity throwableProjectile = throwableEvent.getThrowable();
			return Optional.of(throwableProjectile.getThrower());
		}

		// resolve invoker from GenericProjectileEntity
		Entity projectile = event.getEntity();
		if(projectile instanceof GenericCompositeProjectileEntity) {			
			GenericCompositeProjectileEntity genericProjectile = (GenericCompositeProjectileEntity) projectile;
			return Optional.of(genericProjectile.getThrower());
		}
				
		// return unhandled case
		return Optional.empty();
	}

}
