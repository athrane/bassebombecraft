package bassebombecraft.client.operator.rendering;

import static bassebombecraft.config.ModConfiguration.wildfireEffectDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetInteger1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Create a contagion effect.
 */
public class InitContagion2 implements Operator2 {

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
	BiConsumer<Ports, Vec3[]> bcSetLineVertexes;

	/**
	 * Function to get remaining duration.
	 */
	Function<Ports, Integer> fnGetRemainingDuration;

	/**
	 * Vector array for line vertexes.
	 */
	Vec3[] lineVertexes = new Vec3[2];

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 *
	 * Instance is configured with integer #1 from ports.
	 * 
	 * Instance is configured with vectors #1 as line vertexes from ports.
	 */
	public InitContagion2() {
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetInteger1(), getBcSetVectors1());
	}

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource       function to get source entity.
	 * @param fnGetTarget       function to get target entity.
	 * @param fnGetMax          function to get max value.
	 * @param bcSetLineVertexes function to set line vertexes.
	 */
	public InitContagion2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
			Function<Ports, Integer> fnGetRemainingDuration, BiConsumer<Ports, Vec3[]> bcSetLineVertexes) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.fnGetRemainingDuration = fnGetRemainingDuration;
		this.bcSetLineVertexes = bcSetLineVertexes;
	}

	@Override
	public void run(Ports ports) {
		Entity source = applyV(fnGetSource, ports);
		Entity target = applyV(fnGetTarget, ports);
		double remaining = applyV(fnGetRemainingDuration, ports);
		double max = wildfireEffectDuration.get();

		// calculate end point coordinate
		double pctValue = (max - remaining) / max;

		// positions
		Vec3 sourcePos = source.getBoundingBox().getCenter();
		Vec3 targetPos = target.getBoundingBox().getCenter();

		// float pctValue = oscillateFloat(0, 1);
		double x = Mth.lerp(pctValue, sourcePos.x(), targetPos.x());
		double y = Mth.lerp(pctValue, sourcePos.y(), targetPos.y());
		double z = Mth.lerp(pctValue, sourcePos.z(), targetPos.z());

		// add start point
		lineVertexes[0] = sourcePos;

		// add end point
		lineVertexes[1] = new Vec3(x, y, z);
		ports.setVectors1(lineVertexes);

		// set line vertexes
		bcSetLineVertexes.accept(ports, lineVertexes);
	}

}
