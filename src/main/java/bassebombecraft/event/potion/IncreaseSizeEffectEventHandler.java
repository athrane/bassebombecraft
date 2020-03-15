package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.INCREASE_SIZE_EFFECT;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEffectIsActive;
import bassebombecraft.operator.entity.potion.effect.RemoveEffectAtClient;
import bassebombecraft.potion.effect.IncreaseSizeEffect;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the increased size potion effect.
 * 
 * Server side logic for the {@linkplain IncreaseSizeEffect}.
 * 
 * When {@linkplain PotionExpiryEvent} is received at server side then the
 * removal of the effect is sync'ed to the client side.
 */
@Mod.EventBusSubscriber
public class IncreaseSizeEffectEventHandler {

	/**
	 * Operator execution.
	 */
	static Operators ops;

	static {
		ops = new Operators();
		Operator removeOp = new RemoveEffectAtClient(ops.getSplLivingEntity(), INCREASE_SIZE_EFFECT);
		Operator ifOp = new IfEffectIsActive(ops.getSplLivingEntity(), removeOp, INCREASE_SIZE_EFFECT);
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
