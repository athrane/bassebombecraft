package bassebombecraft.operator.projectile.formation.modifier;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.geom.GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which modifies a set
 * of projectile vectors by rotating them slightly around the Y-axis.
 */
public class InaccuracyProjectileFormationModifier implements Operator2 {

	/**
	 * Degrees for randomisation of projectile.
	 */
	static final int DEGREES_5 = 5
			;

	/**
	 * Function to get orientation vectors.
	 */
	Function<Ports, Vector3d[]> fnGetOrientation;

	/**
	 * Function to set orientation vectors.
	 */
	BiConsumer<Ports, Vector3d[]> bcSetOrientation;

	/**
	 * Constructor.
	 * 
	 * @param fnGetOrientation function to get orientation vectors.
	 * @param fnGetOrientation function to set orientation vectors.
	 */
	public InaccuracyProjectileFormationModifier(Function<Ports, Vector3d[]> fnGetOrientation,
			BiConsumer<Ports, Vector3d[]> bcSetOrientation) {
		this.fnGetOrientation = fnGetOrientation;
		this.bcSetOrientation = bcSetOrientation;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 */
	public InaccuracyProjectileFormationModifier() {
		this(getFnGetVectors1(), getBcSetVectors1());
	}

	@Override
	public void run(Ports ports) {
		Vector3d[] vectors = applyV(fnGetOrientation,ports);

		// get random
		Random random = getBassebombeCraft().getRandom();

		// create new array
		Vector3d[] randomised = new Vector3d[vectors.length];
		
		// create index
		int index = 0;
		
		for (Vector3d orientation : vectors) {

			// calculate random angle
			double angle = random.nextInt(DEGREES_5 * 2) - DEGREES_5;
			randomised[index] = rotateUnitVectorAroundYAxisAtOrigin(angle, orientation);

			index++;
		}

		// store new vectors
		bcSetOrientation.accept(ports, randomised);
	}

}
