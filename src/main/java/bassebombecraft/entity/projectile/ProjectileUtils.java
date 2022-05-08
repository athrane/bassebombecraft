package bassebombecraft.entity.projectile;

import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import java.util.Optional;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

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
	public static boolean isBlockHit(HitResult result) {
		if (result == null)
			return false;
		return (result.getType() == HitResult.Type.BLOCK);
	}

	/**
	 * Return true if entity was hit by ray trace result.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if entity was hit.
	 */
	public static boolean isEntityHit(HitResult result) {
		if (result == null)
			return false;
		return (result.getType() == HitResult.Type.ENTITY);
	}

	/**
	 * Return true if ray trace result missed.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result was a miss.
	 */
	public static boolean isNothingHit(HitResult result) {
		if (result == null)
			return false;
		return (result.getType() == HitResult.Type.MISS);
	}

	/**
	 * Return true if ray trace result a {@linkplain EntityRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain EntityRayTraceResult}.
	 */
	public static boolean isTypeEntityRayTraceResult(HitResult result) {
		if (result == null)
			return false;
		return result instanceof EntityHitResult;
	}

	/**
	 * Return true if ray trace result a {@linkplain BlockRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain BlockRayTraceResult}.
	 */
	public static boolean isTypeBlockRayTraceResult(HitResult result) {
		if (result == null)
			return false;
		return result instanceof BlockHitResult;
	}

	/**
	 * Resolve shooter entity from projectile impact event
	 * {@linkplain ProjectileImpactEvent}.
	 * 
	 * @param event event to resolve projectile shooter from.
	 * 
	 * @return return the shooter entity as a {@linkplain LivingEntity}. If the
	 *         invoker can't be resolved as a living entity then a the returned
	 *         optional is empty.
	 */
	public static Optional<LivingEntity> resolveShooter(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();
		Entity owner = projectile.getOwner(); // Or should it be getOwEffectSource()?

		// return owner as LivingEntity
		if (isTypeLivingEntity(owner))
			return Optional.of((LivingEntity) owner);
		else
			return Optional.empty();
	}

}
