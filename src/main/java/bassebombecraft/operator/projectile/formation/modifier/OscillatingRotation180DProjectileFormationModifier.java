package bassebombecraft.operator.projectile.formation.modifier;

import static bassebombecraft.geom.GeometryUtils.oscillateWithFixedTime;
import static bassebombecraft.geom.GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which modifies a set
 * of projectile vectors by oscillating the [-90,..90] degress around the
 * Y-axis.
 * 
 * Uses the ports counter to hold state.
 */
public class OscillatingRotation180DProjectileFormationModifier implements Operator2 {

	/**
	 * Degrees for rotation of projectile.
	 */
	static final int DEGREES_90 = 90;

	/**
	 * Function to get orientation vectors.
	 */
	Function<Ports, Vec3[]> fnGetOrientation;

	/**
	 * Function to set orientation vectors.
	 */
	BiConsumer<Ports, Vec3[]> bcSetOrientation;

	/**
	 * Constructor.
	 * 
	 * @param fnGetOrientation function to get orientation vectors.
	 * @param fnGetOrientation function to set orientation vectors.
	 */
	public OscillatingRotation180DProjectileFormationModifier(Function<Ports, Vec3[]> fnGetOrientation,
			BiConsumer<Ports, Vec3[]> bcSetOrientation) {
		this.fnGetOrientation = fnGetOrientation;
		this.bcSetOrientation = bcSetOrientation;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 */
	public OscillatingRotation180DProjectileFormationModifier() {
		this(getFnGetVectors1(), getBcSetVectors1());
	}

	@Override
	public void run(Ports ports) {
		Vec3[] vectors = applyV(fnGetOrientation, ports);

		// get counter
		int time = ports.getCounter();

		// get oscillate value
		double oscillatedAngle = oscillateWithFixedTime(time, -DEGREES_90, DEGREES_90);

		// create new array
		Vec3[] rotated = new Vec3[vectors.length];

		// create index
		int index = 0;

		for (Vec3 orientation : vectors) {

			// calculate random angle
			rotated[index] = rotateUnitVectorAroundYAxisAtOrigin(oscillatedAngle, orientation);

			index++;
		}

		// store new vectors
		bcSetOrientation.accept(ports, rotated);

		// update counter
		ports.incrementCounter();
		ports.incrementCounter();
		ports.incrementCounter();
	}

}
