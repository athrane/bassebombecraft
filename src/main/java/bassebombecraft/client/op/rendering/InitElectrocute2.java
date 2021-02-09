package bassebombecraft.client.op.rendering;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;

import java.util.Random;
import java.util.function.Function;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
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

		// add start
		Vec3d sourcePos = source.getPositionVec();
		positions[0] = sourcePos;

		// calculate midpoints
		Vec3d targetPos = target.getPositionVec();		
		Vec3d midpoint0 = calculateArc(sourcePos, targetPos);
		Vec3d midpoint1 = calculateArc(sourcePos, midpoint0);						
		Vec3d midpoint2 = calculateArc(midpoint0, targetPos);						
		
		// add midpoints		
		positions[1] = midpoint1;
		positions[2] = midpoint0;
		positions[3] = midpoint2;
						
		// add end
		positions[4] = targetPos;		
		ports.setVectors1(positions);

		return ports;
	}

	Vec3d calculateArc(Vec3d sourcePos, Vec3d targetPos) {
		Vec3d midpoint = sourcePos.add(targetPos).scale(0.5D);
		
		Random random = getBassebombeCraft().getRandom();		
		double randomX = random.nextDouble() - 0.5F;
		double randomY = random.nextDouble() - 0.5F;
		double randomZ = random.nextDouble() - 0.5F;		
		return midpoint.add(randomX, randomY, randomZ);
	}

}
