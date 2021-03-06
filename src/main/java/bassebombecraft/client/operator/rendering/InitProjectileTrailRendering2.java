package bassebombecraft.client.operator.rendering;

import static bassebombecraft.ClientModConstants.PROJECTILE_TRAIL_LENGTH;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the
 * projectile from entity #1.
 * 
 * The projectile trail is calculated by continuously capturing the positions of
 * entity #1 and adds new positions to create a trail of previous positions.
 * 
 * Adds the target positions as a vector array to the ports.
 */
public class InitProjectileTrailRendering2 implements Operator2 {

	/**
	 * First array index.
	 */
	static final int FIRST_INDEX = 0;

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Function to get line vertexes (as vectors).
	 */
	Function<Ports, Vector3d[]> fnGetLineVertexes;

	/**
	 * Function to set line vertexes (as vectors).
	 */
	BiConsumer<Ports, Vector3d[]> bcSetLineVertexes;

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * Instance is configured with vectors #2 as line vertexes from ports.
	 */
	public InitProjectileTrailRendering2() {
		this(getFnGetEntity1(), getFnGetVectors1(), getBcSetVectors1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile   function to get projectile entity.
	 * @param fnGetLineVertexes function to get line vertexes.
	 * @param bcSetLineVertexes function to set line vertexes.
	 */
	public InitProjectileTrailRendering2(Function<Ports, Entity> fnGetProjectile,
			Function<Ports, Vector3d[]> fnGetLineVertexes, BiConsumer<Ports, Vector3d[]> bcSetLineVertexes) {
		this.fnGetProjectile = fnGetProjectile;
		this.fnGetLineVertexes = fnGetLineVertexes;
		this.bcSetLineVertexes = bcSetLineVertexes;
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);
		Vector3d[] currentVertexes = applyV(fnGetLineVertexes, ports);

		// get current position
		Vector3d currentPosition = projectile.getPositionVec();

		// if no previous position is defined then add position
		if (currentVertexes.length == 0) {
			Vector3d[] vertexes = new Vector3d[1];
			vertexes[FIRST_INDEX] = currentPosition;
			bcSetLineVertexes.accept(ports, vertexes);
			return;
		}

		// get previous position
		Vector3d previousPosition = currentVertexes[0];

		// only add new position if it is different to previous position
		if (isVertexesEqual(currentPosition, previousPosition)) {
			bcSetLineVertexes.accept(ports, currentVertexes);
			return;
		}

		// concatenate vertexes
		Stream<Vector3d> vertexStream = Arrays.stream(currentVertexes);
		Stream<Vector3d> concatStream = Stream.concat(Stream.of(currentPosition),
				vertexStream.limit(PROJECTILE_TRAIL_LENGTH));
		Vector3d[] vertexes = concatStream.toArray(Vector3d[]::new);
		bcSetLineVertexes.accept(ports, vertexes);
	}

	/**
	 * Return if vertexes are equal.
	 * 
	 * @param currentPosition  current position.
	 * @param previousPosition previous position. Might not be defined.
	 * 
	 * @return if vertexes are equal
	 */
	boolean isVertexesEqual(Vector3d currentPosition, Vector3d previousPosition) {
		if (previousPosition == null)
			return false;
		return currentPosition.equals(previousPosition);
	}

}
