package bassebombecraft.client.operator.rendering;

import static bassebombecraft.config.ModConfiguration.wildfireEffectDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetInteger1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which reads the source
 * from entity #1 and target from entity #2.
 * 
 * Create a travelling fire effect. The positions of the fire arc are stored as
 * line vertexes in the ports.
 */
public class InitWildfire2 implements Operator2 {

	/**
	 * Noise range for points.
	 */
	static final double POINT_NOISE = 1.0D;

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
	public InitWildfire2() {
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
	public InitWildfire2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget,
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
		Vec3 startPos = new Vec3(x, y, z);
		lineVertexes[0] = startPos;

		// add end point
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		Vec3 endPos = addNoiseToPosition(startPos, POINT_NOISE, random);
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
	Vec3 addNoiseToPosition(Vec3 source, double noise, Random random) {
		double randomX = random.nextDouble() - 0.5F;
		double randomY = random.nextDouble() - 0.5F;
		double randomZ = random.nextDouble() - 0.5F;
		return source.add(noise * randomX, noise * randomY, noise * randomZ);
	}

}
