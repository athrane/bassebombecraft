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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

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
	Function<Ports, Vec3[]> fnGetLineVertexes;

	/**
	 * Function to set line vertexes (as vectors).
	 */
	BiConsumer<Ports, Vec3[]> bcSetLineVertexes;

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
			Function<Ports, Vec3[]> fnGetLineVertexes, BiConsumer<Ports, Vec3[]> bcSetLineVertexes) {
		this.fnGetProjectile = fnGetProjectile;
		this.fnGetLineVertexes = fnGetLineVertexes;
		this.bcSetLineVertexes = bcSetLineVertexes;
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);
		Vec3[] currentVertexes = applyV(fnGetLineVertexes, ports);

		// get current position
		Vec3 currentPosition = projectile.position();

		// if no previous position is defined then add position
		if (currentVertexes.length == 0) {
			Vec3[] vertexes = new Vec3[1];
			vertexes[FIRST_INDEX] = currentPosition;
			bcSetLineVertexes.accept(ports, vertexes);
			return;
		}

		// get previous position
		Vec3 previousPosition = currentVertexes[0];

		// only add new position if it is different to previous position
		if (isVertexesEqual(currentPosition, previousPosition)) {
			bcSetLineVertexes.accept(ports, currentVertexes);
			return;
		}

		// concatenate vertexes
		Stream<Vec3> vertexStream = Arrays.stream(currentVertexes);
		Stream<Vec3> concatStream = Stream.concat(Stream.of(currentPosition),
				vertexStream.limit(PROJECTILE_TRAIL_LENGTH));
		Vec3[] vertexes = concatStream.toArray(Vec3[]::new);
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
	boolean isVertexesEqual(Vec3 currentPosition, Vec3 previousPosition) {
		if (previousPosition == null)
			return false;
		return currentPosition.equals(previousPosition);
	}

}
