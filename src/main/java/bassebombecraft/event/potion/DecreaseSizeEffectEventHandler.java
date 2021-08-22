package bassebombecraft.event.potion;

import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.potion.effect.RegisteredEffects.DECREASE_SIZE_EFFECT;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEffectActive2;
import bassebombecraft.operator.conditional.IsWorldAtServerSide2;
import bassebombecraft.operator.entity.potion.effect.RemoveEffectAtClient2;
import bassebombecraft.potion.effect.DecreaseSizeEffect;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the decrease size potion effect. Logic for the
 * {@linkplain DecreaseSizeEffect}.
 * 
 * When {@linkplain PotionExpiryEvent} is received at server side then the
 * removal of the effect is sync'ed to the client side.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class DecreaseSizeEffectEventHandler {

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new IsWorldAtServerSide2(), new IsEffectActive2(DECREASE_SIZE_EFFECT.get()),
				new RemoveEffectAtClient2(DECREASE_SIZE_EFFECT.get()));
	};

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);

	/**
	 * Handle {@linkplain PotionExpiryEvent} at server side.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void handlePotionExpiryEvent(PotionExpiryEvent event) {
		Ports ports = getInstance();
		ports.setLivingEntity1(event.getEntityLiving());
		run(ports, lazyInitOp);
	}

}
