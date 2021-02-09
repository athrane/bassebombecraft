package bassebombecraft.client.op.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Create a lightning effect .. positions as a vector array to the ports.
 */
public class InitElectrocute2 implements Operator2 {

	/**
	 * Noise range for midpoints.
	 */
	static final double MIDPOINT_NOISE = 1.5D;

	/**
	 * Noise range for end points.
	 */
	static final double ENDPOINT_NOISE = 1.0D;
	
	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, Entity> fnGetTarget;

	/**
	 * VEctor array for positions.
	 */
	Vec3d[] positions = new Vec3d[5];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 */
	public InitElectrocute2() {
		this(getFnGetEntity1(), getFnGetEntity2());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity.
	 * @param fnGetTarget function to get target entity.
	 */
	public InitElectrocute2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
	}

	@Override
	public Ports run(Ports ports) {

		// get source
		Entity source = fnGetSource.apply(ports);
		if (source == null)
			return ports;

		// get target
		Entity target = fnGetTarget.apply(ports);
		if (target == null)
			return ports;

		// get random
		Random random = getBassebombeCraft().getRandom();
		
		// add start point
		Vec3d sourcePos = source.getBoundingBox().getCenter();
		sourcePos = addNoiseToPosition(sourcePos, ENDPOINT_NOISE, random);

		positions[0] = sourcePos;

		// calculate midpoints
		Vec3d targetPos = target.getBoundingBox().getCenter();
		Vec3d midpoint0 = calculateArcNode(sourcePos, targetPos, MIDPOINT_NOISE, random);
		Vec3d midpoint1 = calculateArcNode(sourcePos, midpoint0, MIDPOINT_NOISE, random);
		Vec3d midpoint2 = calculateArcNode(midpoint0, targetPos, MIDPOINT_NOISE, random);

		// add midpoints
		positions[1] = midpoint1;
		positions[2] = midpoint0;
		positions[3] = midpoint2;

		// add end point
		targetPos = addNoiseToPosition(targetPos, ENDPOINT_NOISE, random);		
		positions[4] = targetPos;
		ports.setVectors1(positions);

		return ports;
	}

	/**
	 * Calculate displaced arc vertex.
	 * 
	 * @param source source position.
	 * @param target target position.
	 * @param noise noise scale.
	 * @param random random generator.
	 * 
	 * @return displaced arc vertex.
	 */
	Vec3d calculateArcNode(Vec3d source, Vec3d target, double noise, Random random) {
		Vec3d midpoint = source.add(target).scale(0.5D);
		return addNoiseToPosition(midpoint, noise, random);
	}

	/**
	 * Add noise to position.
	 * 
	 * @param source source position.
	 * @param random random generator.
	 * @param noise noise scale.
	 * 
	 * @return noisy position.
	 */
	Vec3d addNoiseToPosition(Vec3d source, double noise, Random random) {
		double randomX = random. nextDouble() - 0.5F;
		double randomY = random.nextDouble() - 0.5F;
		double randomZ = random.nextDouble() - 0.5F;
		return source.add(noise * randomX, noise * randomY, noise * randomZ);
	}
	
}
