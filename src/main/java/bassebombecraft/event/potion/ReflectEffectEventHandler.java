package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.REFLECT_EFFECT;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.potion.PotionUtils.getEffectIfActive;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEffectActive2;
import bassebombecraft.operator.conditional.IsWorldAtServerSide2;
import bassebombecraft.operator.entity.ReflectMobDamageAmplified2;
import bassebombecraft.potion.effect.ReflectEffect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the reflect potion effect.
 * 
 * Logic for the {@linkplain ReflectEffect}.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class ReflectEffectEventHandler {

	/**
	 * Null effect instance.
	 */
	static final EffectInstance NO_EFFECT = null;

	/**
	 * Operator instance (can be null due to class loading).
	 */
	static Optional<Operator2> optOp = Optional.empty();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {

		// ReflectMobDamageAmplified2: get damage source (from event)
		Function<Ports, DamageSource> fnGetDamageSource = DefaultPorts.getFnDamageSource1();

		// ReflectMobDamageAmplified2: get amplifier from effect instance
		Function<Ports, Integer> fnGetAmplifier = p -> getEffectIfActive(p.getLivingEntity1(), REFLECT_EFFECT).get()
				.getAmplifier();

		// ReflectMobDamageAmplified2: get damage amount from event
		Function<Ports, Float> fnGetAmount = p -> p.getDouble1().floatValue();

		Operator2 op = new Sequence2(new IsWorldAtServerSide2(), new IsEffectActive2(REFLECT_EFFECT),
				new ReflectMobDamageAmplified2(fnGetDamageSource, fnGetAmplifier, fnGetAmount));
		optOp = Optional.of(op);
		return optOp.get();
	};

	@SubscribeEvent
	public static void handleLivingDamageEvent(LivingDamageEvent event) {
		Ports ports = getInstance();
		ports.setLivingEntity1(event.getEntityLiving());
		ports.setDamageSource1(event.getSource());
		ports.setDouble1((double) event.getAmount());
		run(ports, optOp.orElseGet(splOp));
	}

}
