package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.config.ModConfiguration.explodeMinExplosionRadius;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.world.level.Explosion.BlockInteraction.DESTROY;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which creates
 * explosion on impact.
 */
public class ExplodeOnImpact2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = ExplodeOnImpact2.class.getSimpleName();

	/**
	 * Null entity for block explosions.
	 */
	static final Entity NULL_ENTITY = null;

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, Level> fnGetWorld;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetWorld        function to get world.
	 */
	public ExplodeOnImpact2(Function<Ports, HitResult> fnGetRayTraceResult, Function<Ports, Level> fnGetWorld) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetWorld = fnGetWorld;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 */
	public ExplodeOnImpact2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);
		Level world = applyV(fnGetWorld, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// create explosion at hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityHitResult) result).getEntity();

			// get position of hit entity
			BlockPos position = entity.blockPosition();

			// calculate explosion radius
			AABB aabb = entity.getBoundingBox();
			float explosionRadius = (float) Math.max(aabb.getXsize(), aabb.getZsize());
			double minExplosionRadius = explodeMinExplosionRadius.get();
			explosionRadius = (float) Math.max(explosionRadius, minExplosionRadius);

			// create explosion
			world.explode(entity, position.getX(), position.getY(), position.getZ(), explosionRadius, DESTROY);
		}

		// create explosion at hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockHitResult blockResult = (BlockHitResult) result;

			// calculate set of block directives
			BlockPos position = blockResult.getBlockPos();

			// create explosion
			double minExplosionRadius = explodeMinExplosionRadius.get();
			world.explode(NULL_ENTITY, position.getX(), position.getY(), position.getZ(),
					(float) minExplosionRadius, DESTROY);
		}
	}

}
