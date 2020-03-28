package bassebombecraft.operator.entity;

import static bassebombecraft.entity.EntityUtils.selfDestruct;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator} interface which destroys entity.
 */
public class SelfDestruct implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SelfDestruct.class.getSimpleName();

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Constructor.
	 * 
	 * @param splEntity invoker entity supplier.
	 */
	public SelfDestruct(Supplier<LivingEntity> splEntity) {
		this.splEntity = splEntity;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		selfDestruct(livingEntity);
	}
}
