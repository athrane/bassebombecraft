package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Implementation of the {@linkplain Operator} interface which removes effect at
 * client side.
 */
public class RemoveEffectAtClient implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = RemoveEffectAtClient.class.getSimpleName();

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Effect.
	 */
	Effect effect;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param effect    effect.
	 */
	public RemoveEffectAtClient(Supplier<LivingEntity> splEntity, Effect effect) {
		this.splEntity = splEntity;
		this.effect = effect;
	}

	@Override
	public void run() {
		try {
			// get entity
			LivingEntity entity = splEntity.get();

			// sync effect to client
			getProxy().getNetworkChannel().sendRemoveEffectPacket(entity, effect);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
