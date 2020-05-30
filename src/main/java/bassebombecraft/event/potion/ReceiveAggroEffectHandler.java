package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.DECOY;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityAttributeIsDefined;
import bassebombecraft.operator.entity.SelfDestruct;
import bassebombecraft.potion.effect.ReceiveAggroEffect;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the receive aggro potion effect.
 * 
 * Logic for the {@linkplain ReceiveAggroEffect}.
 * 
 * When {@linkplain PotionExpiryEvent} is received at server side then the
 * entity is killed if it is a decoy.
 * 
 * The handler only executes events SERVER side. 
 */
@Mod.EventBusSubscriber
public class ReceiveAggroEffectHandler {

	/**
	 * Operator execution.
	 */
	static Operators ops;

	static {
		ops = new Operators();
		Operator destructOp = new SelfDestruct(ops.getSplLivingEntity());
		Operator ifOp = new IfEntityAttributeIsDefined(ops.getSplLivingEntity(), destructOp, DECOY);
		ops.setOperator(ifOp);
	}

	/**
	 * Handle {@linkplain PotionExpiryEvent} at server side.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void handlePotionExpiryEvent(PotionExpiryEvent event) {
		ops.run(event.getEntityLiving());
	}

}
