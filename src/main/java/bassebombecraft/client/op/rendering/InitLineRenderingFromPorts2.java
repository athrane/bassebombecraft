package bassebombecraft.client.op.rendering;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Adds the source and target positions as a vector array to the ports.
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
	 * VEctor array for positions.
	 */
	Vec3d[] positions = new Vec3d[2];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 */
	public InitLineRenderingFromPorts2() {
		this(getFnGetEntity1(), getFnGetEntity2());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity.
	 * @param fnGetTarget function to get target entity.
	 */
	public InitLineRenderingFromPorts2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget) {
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

		// add positions as vector 0 and 1
		positions[0] = source.getPositionVec();
		positions[1] = target.getPositionVec();
		ports.setVectors1(positions);

		return ports;
	}

}
