package bassebombecraft.client.operator.rendering;

import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Adds the source and target positions as a line vertexes in the ports.
 */
public class InitLineRenderingFromPorts2 implements Operator2 {

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
	BiConsumer<Ports, Vec3d[]> bcSetLineVertexes;

	/**
	 * Vector array for line vertexes.
	 */
	Vec3d[] lineVertexes = new Vec3d[2];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 */
	public InitLineRenderingFromPorts2() {
		this(getFnGetEntity1(), getFnGetEntity2(), getBcSetVectors1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource       function to get source entity.
	 * @param fnGetTarget       function to get target entity.
	 * @param bcSetLineVertexes function to set line vertexes.
	 */
	public InitLineRenderingFromPorts2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
			BiConsumer<Ports, Vec3d[]> bcSetLineVertexes) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.bcSetLineVertexes = bcSetLineVertexes;
	}

	@Override
	public void run(Ports ports) {
		Entity source = applyV(fnGetSource, ports);
		Entity target = applyV(fnGetTarget, ports);

		// add positions as vertex 0 and 1
		lineVertexes[0] = source.getPositionVec();
		lineVertexes[1] = target.getPositionVec();

		// set line vertexes
		bcSetLineVertexes.accept(ports, lineVertexes);
	}

}
