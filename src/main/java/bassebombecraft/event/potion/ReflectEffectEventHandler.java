package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.REFLECT_EFFECT;
import static bassebombecraft.operator.conditional.IfEffectIsActive.getInstance;
import static bassebombecraft.operator.event.ReflectMobDamageAmplified.getInstance;
import static bassebombecraft.potion.PotionUtils.getEffectIfActive;

import java.util.Optional;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.potion.effect.ReflectEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the reflect potion effect.
 * 
 * Logic for the {@linkplain ReflectEffect}.
 */
@Mod.EventBusSubscriber
public class ReflectEffectEventHandler {

	/**
	 * Null effect instance.
	 */
	static final EffectInstance NO_EFFECT = null;

	/**
	 * Operator execution.
	 */
	static Operators operators;

	static {
		operators = new Operators();
		Operator reflectOp = getInstance(operators.getSplLivingDamageEvent(), operators.getSplEffectInstance());
		Operator ifOp = getInstance(operators.getSplLivingEntity(), reflectOp, REFLECT_EFFECT);
		operators.setOperator(ifOp);
	}

	@SubscribeEvent
	public static void handleLivingDamageEvent(LivingDamageEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Optional<EffectInstance> optEffect = getEffectIfActive(livingEntity, REFLECT_EFFECT);
		operators.run(event, event.getEntityLiving(), optEffect.orElse(NO_EFFECT));
	}

}
