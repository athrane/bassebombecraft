package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which spawns an ice
 * block around the hit mob.
 * 
 * If a block is hit then a temporary ice block is created where the projectile
 * hit.
 */
public class SpawnSquid2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnSquid2.class.getSimpleName();

	/**
	 * Spawn Y offset.
	 */
	static final int Y_SPAWN_OFFSET = 2;

	/**
	 * Squid pitch.
	 */
	static final float PITCH = 0.0F;

	/**
	 * Entity yaw.
	 */
	static final float PARTIAL_TICKS = 1.0F;

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
	public SpawnSquid2(Function<Ports, HitResult> fnGetRayTraceResult, Function<Ports, Level> fnGetWorld) {
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
	public SpawnSquid2() {
		this(getFnGetRayTraceResult1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);
		Level world = applyV(fnGetWorld, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare variables
		Vec3 posVec = null;
		float yaw = 0;

		// spawn squid around entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityHitResult) result).getEntity();

			// get entity position
			posVec = entity.position();
			posVec = posVec.add(0, entity.getBbHeight(), 0);

			// get entity yaw
			yaw = entity.getViewYRot(PARTIAL_TICKS);
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockHitResult blockResult = (BlockHitResult) result;

			// calculate spawn position
			BlockPos spawnPosition = calculatePosition(blockResult);
			posVec = new Vec3(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
		}

		// spawn squid
		Squid entity = EntityType.SQUID.create(world);
		double lx = posVec.x;
		double ly = posVec.y + Y_SPAWN_OFFSET;
		double lz = posVec.z;
		entity.moveTo(lx, ly, lz, yaw, PITCH);
		world.addFreshEntity(entity);
	}

}
