package bassebombecraft.operator.projectile.path;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which move the
 * projectile alongs its path in a zig zag pattern
 * 
 * The projectile motion vector is updated.
 */
public class ZigZagProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = ZigZagProjectilePath.class.getSimpleName();

	/**
	 * Period in ticks between changing angles.
	 */
	static final long PERIOD = 25;

	/**
	 * Square function amplitude.
	 */
	static final long AMPLITUDE = 1;

	/**
	 * Rotation when zig zaging.
	 */
	static final int ROTATION = 90;
	
	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public ZigZagProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * The operator also uses the port counter.
	 */
	public ZigZagProjectilePath() {
		this(getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vec3 motionVector = projectile.getDeltaMovement();
		if (motionVector == null)
			return;

		// calculate angle using triangle wave function
		long x = ports.getCounter();

		// different values, then rotate the projectile
		double angleDegrees = calculateAngle(x);

		// exit if no rotation
		if (angleDegrees == 0)
			return;

		// rotate
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vec3 newMotionVector = motionVector.yRot(angleRadians);

		// update motion
		projectile.setDeltaMovement(newMotionVector.x(), newMotionVector.y(), newMotionVector.z());
	}

	/**
	 * Calculate angle for next rotation.
	 * 
	 * @param x x parameter for the next function value.
	 * 
	 * @return angle for next rotation.
	 */
	double calculateAngle(long x) {

		// tiny rotation if first tick
		if (x == 0)
			return -(ROTATION/2);

		double vx = squareWave(x);
		double vxm1 = squareWave(x - 1);
		
		// no rotation if values are equal
		if(vx == vxm1) return 0;
		
		// positive rotation
		if (vx == AMPLITUDE)
			return -ROTATION;

		// negative rotation
		return ROTATION;
	}

	/**
	 * Calculate square wave.
	 * 
	 * @param x value to calculate wave from.
	 * 
	 * @return square wave value.
	 */
	long squareWave(long x) {
		long value = (x % PERIOD);
		if (value < (PERIOD/2))
			return AMPLITUDE;		
		return 0;
	}

}
