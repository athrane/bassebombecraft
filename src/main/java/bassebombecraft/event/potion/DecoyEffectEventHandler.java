package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.DECOY_EFFECT;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IfEffectIsActive;
import bassebombecraft.operator.entity.SelfDestruct;
import bassebombecraft.operator.entity.potion.effect.RemoveEffectAtClient;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the decoy potion effect.
 * 
 * Server side logic for the {@linkplain DecoySizeEffect}.
 * 
 * When {@linkplain PotionExpiryEvent} is received at server side then the
 * removal of the effect is sync'ed to the client side.
 */
@Mod.EventBusSubscriber
public class DecoyEffectEventHandler {

	/**
	 * Operator execution.
	 */
	static Operators ops;

	static {
		ops = new Operators();
		Operator removeOp = new RemoveEffectAtClient(ops.getSplLivingEntity(), DECOY_EFFECT);
		Operator destructOp = new SelfDestruct(ops.getSplLivingEntity());
		Operator opSeq = new Sequence2(removeOp, destructOp);		
		Operator ifOp = new IfEffectIsActive(ops.getSplLivingEntity(), opSeq, DECOY_EFFECT);
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
