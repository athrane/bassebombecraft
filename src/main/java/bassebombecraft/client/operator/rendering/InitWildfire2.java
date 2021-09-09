package bassebombecraft.client.operator.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.geom.GeometryUtils.oscillateFloat;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Create a travelling fire effect. The positions of the fire arc are stored as
 * line vertexes in the ports.
 */
public class InitWildfire2 implements Operator2 {

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
	Vector3d[] lineVertexes = new Vector3d[2];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 */
	public InitWildfire2() {
		this(getFnGetEntity1(), getFnGetEntity2(), getBcSetVectors1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource       function to get source entity.
	 * @param fnGetTarget       function to get target entity.
	 * @param bcSetLineVertexes function to set line vertexes.
	 */
	public InitWildfire2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
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
		
		// positions
		Vector3d sourcePos = source.getBoundingBox().getCenter();
		Vector3d targetPos = target.getBoundingBox().getCenter();				
		
		// calculate end point coordinate
		float oscValue = oscillateFloat(0, 1);		
		double x = MathHelper.lerp(oscValue, sourcePos.getX(), targetPos.getX());
		double y = MathHelper.lerp(oscValue, sourcePos.getY(), targetPos.getY());
		double z = MathHelper.lerp(oscValue, sourcePos.getZ(), targetPos.getZ());
		
		// add start point
		lineVertexes[0] = sourcePos;
		
		// add end point
		Vector3d endPos = new Vector3d(x, y, z);
		endPos = addNoiseToPosition(endPos, ENDPOINT_NOISE, random);
		lineVertexes[1] = endPos;
		ports.setVectors1(lineVertexes);

		// set line vertexes
		bcSetLineVertexes.accept(ports, lineVertexes);
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
