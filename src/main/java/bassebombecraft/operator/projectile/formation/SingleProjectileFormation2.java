package bassebombecraft.operator.projectile.formation;

import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots a single projectile
 * from the invoker view vector.
 */
public class SingleProjectileFormation2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SingleProjectileFormation2.class.getSimpleName();

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.1D;

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to set vector array.
	 */
	BiConsumer<Ports, Vec3[]> bcSetVectors;

	/**
	 * Array to store acceleration vector.
	 */
	Vec3[] vectorArray;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 * @param bcSetVectors function to set vector array
	 */
	public SingleProjectileFormation2(Function<Ports, LivingEntity> fnGetInvoker,
			BiConsumer<Ports, Vec3[]> bcSetVectors) {
		this.fnGetInvoker = fnGetInvoker;
		this.bcSetVectors = bcSetVectors;
		this.vectorArray = new Vec3[1];
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vectors array #1 as orientation vectors from ports.
	 */
	public SingleProjectileFormation2() {
		this(getFnGetLivingEntity1(), getBcSetVectors1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);

		// calculate orientation
		Vec3 orientation = invoker.getViewVector(1).scale(ACCELERATION_MODIFIER);

		// update array
		vectorArray[0] = orientation;

		// store vector array in ports
		bcSetVectors.accept(ports, vectorArray);
	}

}
