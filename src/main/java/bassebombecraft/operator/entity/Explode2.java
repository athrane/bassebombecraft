package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.world.Explosion.Mode.DESTROY;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import static bassebombecraft.config.ModConfiguration.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which creates an
 * explosion from a hit target.
 */
public class Explode2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Explode2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, Entity> fnGetTarget;

	/**
	 * Constructor.
	 * 
	 * @param fnGetTarget function to get target entity.
	 */
	public Explode2(Function<Ports, Entity> fnGetTarget) {
		this.fnGetTarget = fnGetTarget;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as target from ports.
	 */
	public Explode2() {
		this(getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		Entity target = applyV(fnGetTarget, ports);

		// get position of dead entity
		BlockPos position = target.blockPosition();

		// get world
		Level world = target.getCommandSenderWorld();

		// calculate explosion radius
		AABB aabb = target.getBoundingBox();
		float explosionRadius = (float) Math.max(aabb.getXsize(), aabb.getZsize());
		explosionRadius = (float) Math.max(explosionRadius, explodeMinExplosionRadius.get());

		// create explosion
		world.explode(target, position.getX(), position.getY(), position.getZ(), explosionRadius, DESTROY);
	}

}
