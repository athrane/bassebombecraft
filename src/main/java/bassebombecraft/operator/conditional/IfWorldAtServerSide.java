package bassebombecraft.operator.conditional;

import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if the embedded world is at server side.
 */
public class IfWorldAtServerSide implements Operator {

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param operator  embedded operator which is executed if effect is active.
	 */
	public IfWorldAtServerSide(Supplier<LivingEntity> splEntity, Operator operator) {
		this.splEntity = splEntity;
		this.operator = operator;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity entity = splEntity.get();

		// exit if handler is executed at client side
		if (isWorldAtClientSide(entity))
			return;

		operator.run();
	}

}
