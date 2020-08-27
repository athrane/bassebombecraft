package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.config.ModConfiguration.explodeMinExplosionRadius;
import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.world.Explosion.Mode.DESTROY;

import java.util.function.Function;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld;

	/**
	 * Minimum explosion radius.
	 */
	double minExplosionRadius;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetWorld        function to get world.
	 */
	public ExplodeOnImpact2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetWorld = fnGetWorld;
		minExplosionRadius = explodeMinExplosionRadius.get();
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
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);

		// get world
		World world = fnGetWorld.apply(ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// create explosion at hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return ports;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position of hit entity
			BlockPos position = entity.getPosition();

			// calculate explosion radius
			AxisAlignedBB aabb = entity.getBoundingBox();
			float explosionRadius = (float) Math.max(aabb.getXSize(), aabb.getZSize());
			explosionRadius = (float) Math.max(explosionRadius, minExplosionRadius);

			// create explosion
			world.createExplosion(entity, position.getX(), position.getY(), position.getZ(), explosionRadius, DESTROY);

			return ports;
		}

		// create explosion at hit block
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return ports;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// calculate set of block directives
			BlockPos position = blockResult.getPos();

			// create explosion
			float explosionRadius = (float) minExplosionRadius;
			world.createExplosion(NULL_ENTITY, position.getX(), position.getY(), position.getZ(), explosionRadius,
					DESTROY);
		}

		return ports;
	}

	/**
	 * Register block for processed to generate air block.
	 * 
	 * @param aabb  AABB
	 * @param pos   block position to process.
	 * @param world world where directive should be processed.
	 */
	void registerBlockToDig(AxisAlignedBB aabb, BlockPos pos, World world) {
		double translateY = aabb.maxY - aabb.minY;
		BlockPos tranlatedPos = pos.add(0, -translateY, 0);
		BlockDirective directive = getInstance(tranlatedPos, AIR, DONT_HARVEST, world);
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);
	}

}
