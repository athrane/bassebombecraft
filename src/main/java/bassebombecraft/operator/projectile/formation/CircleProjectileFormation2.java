package bassebombecraft.operator.projectile.formation;

import static bassebombecraft.config.ModConfiguration.circleProjectileFormationNumberProjectiles;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots
 * projectiles in a ring formation.
 */
public class CircleProjectileFormation2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = CircleProjectileFormation2.class.getSimpleName();

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.1D;

	/**
	 * Initial vector.
	 */
	static final Vec3d INITIAL_VECTOR = new Vec3d(1, 0, 0).scale(ACCELERATION_MODIFIER);

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to set vector array.
	 */
	BiConsumer<Ports, Vec3d[]> bcSetVectors;

	/**
	 * Array to store acceleration vectors.
	 */
	Vec3d[] vectors;

	/**
	 * Number of fireballs in ring.
	 */
	int number;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 * @param bcSetVectors function to set vector array
	 */
	public CircleProjectileFormation2(Function<Ports, LivingEntity> fnGetInvoker,
			BiConsumer<Ports, Vec3d[]> bcSetVectors) {
		this.fnGetInvoker = fnGetInvoker;
		this.bcSetVectors = bcSetVectors;
		number = circleProjectileFormationNumberProjectiles.get();
		this.vectors = new Vec3d[number];
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vectors array #1 as orientation vectors from
	 * ports.
	 */
	public CircleProjectileFormation2() {
		this(getFnGetLivingEntity1(), getBcSetVectors1());
	}

	@Override
	public Ports run(Ports ports) {
		int displacement = 360 / number;

		// get invoker
		// LivingEntity invoker = fnGetInvoker.apply(ports);

		for (int index = 0; index < number; index++) {

			// calculate acceleration
			double angleRadians = Math.toRadians(index * displacement);
			Vec3d orientation = INITIAL_VECTOR.rotateYaw((float) angleRadians);

			vectors[index] = orientation;
		}

		// store vector array in ports
		bcSetVectors.accept(ports, vectors);

		return ports;
	}

}
