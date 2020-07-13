package bassebombecraft.operator.projectile.formation;

import static bassebombecraft.operator.DefaultPorts.getBcSetVectors1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static bassebombecraft.geom.GeometryUtils.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots three
 * projectiles. One projectile is shoot forward from the invoker view vector and
 * two spread to the side.
 */
public class TrifurcatedProjectileFormation2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = TrifurcatedProjectileFormation2.class.getSimpleName();
		
	/**
	 * Rotation angle.
	 */	
	static final int ROTATE_DEGREES = 10;
	
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
	BiConsumer<Ports, Vec3d[]> bcSetVectors;

	/**
	 * Array to store acceleration vector.
	 */
	Vec3d[] vectorArray;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 * @param bcSetVectors function to set vector array
	 */
	public TrifurcatedProjectileFormation2(Function<Ports, LivingEntity> fnGetInvoker,
			BiConsumer<Ports, Vec3d[]> bcSetVectors) {
		this.fnGetInvoker = fnGetInvoker;
		this.bcSetVectors = bcSetVectors;
		this.vectorArray = new Vec3d[3];
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vectors array #1 as orientation vectors from
	 * ports.
	 */
	public TrifurcatedProjectileFormation2() {
		this(getFnGetLivingEntity1(), getBcSetVectors1());
	}

	@Override
	public Ports run(Ports ports) {

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// calculate orientation
		Vec3d orientation = invoker.getLook(1).scale(ACCELERATION_MODIFIER);

		// update array
		vectorArray[0] = orientation;
		vectorArray[1] = rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES, orientation);
		vectorArray[2] = rotateUnitVectorAroundYAxisAtOrigin(-ROTATE_DEGREES, orientation);
		
		// store vector array in ports
		bcSetVectors.accept(ports, vectorArray);

		return ports;
	}

}
