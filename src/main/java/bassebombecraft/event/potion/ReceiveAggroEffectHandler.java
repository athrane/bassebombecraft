package bassebombecraft.event.potion;

import static bassebombecraft.entity.attribute.RegisteredAttributes.IS_DECOY_ATTRIBUTE;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.run;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityAttributeSet2;
import bassebombecraft.operator.conditional.IsWorldAtServerSide2;
import bassebombecraft.operator.entity.SelfDestruct2;
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
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new IsWorldAtServerSide2(), new IsEntityAttributeSet2(IS_DECOY_ATTRIBUTE.get()),
				new SelfDestruct2());
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
