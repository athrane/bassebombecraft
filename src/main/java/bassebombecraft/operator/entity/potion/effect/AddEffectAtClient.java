package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator} interface which adds effect at
 * client side.
 */
public class AddEffectAtClient implements Operator {

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Effect instance supplier.
	 */
	Supplier<EffectInstance> splEffectInstance;

	/**
	 * Constructor.
	 * 
	 * @param splEntity entity supplier.
	 * @param effect    effect.
	 */
	public AddEffectAtClient(Supplier<LivingEntity> splEntity, Supplier<EffectInstance> splEffectInstance) {
		this.splEntity = splEntity;
		this.splEffectInstance = splEffectInstance;
	}

	@Override
	public void run() {
		try {
			// get entity
			LivingEntity entity = splEntity.get();
			EffectInstance effectInstance = splEffectInstance.get();

			// sync effect to client
			getProxy().getNetworkChannel(entity.getEntityWorld()).sendAddEffectPacket(entity, effectInstance);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
