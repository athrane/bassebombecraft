package bassebombecraft.client.operator.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Create a lightning effect. The positions of the lightning arc are stored as
 * line vertexes in the ports.
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
	 * Function to set line vertexes (as vectors).
	 */
	BiConsumer<Ports, Vector3d[]> bcSetLineVertexes;

	/**
	 * Vector array for line vertexes.
	 */
	Vector3d[] lineVertexes = new Vector3d[5];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 */
	public InitElectrocute2() {
		this(getFnGetEntity1(), getFnGetEntity2(), getBcSetVectors1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource       function to get source entity.
	 * @param fnGetTarget       function to get target entity.
	 * @param bcSetLineVertexes function to set line vertexes.
	 */
	public InitElectrocute2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
			BiConsumer<Ports, Vector3d[]> bcSetLineVertexes) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.bcSetLineVertexes = bcSetLineVertexes;
	}

	@Override
	public void run(Ports ports) {
		Entity source = applyV(fnGetSource, ports);
		Entity target = applyV(fnGetTarget, ports);

		// get random
		Random random = getBassebombeCraft().getRandom();

		// add start point
		Vector3d sourcePos = source.getBoundingBox().getCenter();
		sourcePos = addNoiseToPosition(sourcePos, ENDPOINT_NOISE, random);

		lineVertexes[0] = sourcePos;

		// calculate midpoints
		Vector3d targetPos = target.getBoundingBox().getCenter();
		Vector3d midpoint0 = calculateArcNode(sourcePos, targetPos, MIDPOINT_NOISE, random);
		Vector3d midpoint1 = calculateArcNode(sourcePos, midpoint0, MIDPOINT_NOISE, random);
		Vector3d midpoint2 = calculateArcNode(midpoint0, targetPos, MIDPOINT_NOISE, random);

		// add midpoints
		lineVertexes[1] = midpoint1;
		lineVertexes[2] = midpoint0;
		lineVertexes[3] = midpoint2;

		// add end point
		targetPos = addNoiseToPosition(targetPos, ENDPOINT_NOISE, random);
		lineVertexes[4] = targetPos;
		ports.setVectors1(lineVertexes);

		// set line vertexes
		bcSetLineVertexes.accept(ports, lineVertexes);
	}

	/**
	 * Calculate displaced arc vertex.
	 * 
	 * @param source source position.
	 * @param target target position.
	 * @param noise  noise scale.
	 * @param random random generator.
	 * 
	 * @return displaced arc vertex.
	 */
	Vector3d calculateArcNode(Vector3d source, Vector3d target, double noise, Random random) {
		Vector3d midpoint = source.add(target).scale(0.5D);
		return addNoiseToPosition(midpoint, noise, random);
	}

	/**
	 * Add noise to position.
	 * 
	 * @param source source position.
	 * @param random random generator.
	 * @param noise  noise scale.
	 * 
	 * @return noisy position.
	 */
	Vector3d addNoiseToPosition(Vector3d source, double noise, Random random) {
		double randomX = random.nextDouble() - 0.5F;
		double randomY = random.nextDouble() - 0.5F;
		double randomZ = random.nextDouble() - 0.5F;
		return source.add(noise * randomX, noise * randomY, noise * randomZ);
	}

}
