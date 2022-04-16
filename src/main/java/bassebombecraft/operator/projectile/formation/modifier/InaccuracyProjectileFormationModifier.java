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
import net.minecraft.world.phys.Vec3;

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
	public InaccuracyProjectileFormationModifier(Function<Ports, Vec3[]> fnGetOrientation,
			BiConsumer<Ports, Vec3[]> bcSetOrientation) {
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
		Vec3[] vectors = applyV(fnGetOrientation,ports);

		// get random
		Random random = getBassebombeCraft().getRandom();

		// create new array
		Vec3[] randomised = new Vec3[vectors.length];
		
		// create index
		int index = 0;
		
		for (Vec3 orientation : vectors) {

			// calculate random angle
			double angle = random.nextInt(DEGREES_5 * 2) - DEGREES_5;
			randomised[index] = rotateUnitVectorAroundYAxisAtOrigin(angle, orientation);

			index++;
		}

		// store new vectors
		bcSetOrientation.accept(ports, randomised);
	}

}
