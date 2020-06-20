package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.REFLECT_EFFECT;
import static bassebombecraft.potion.PotionUtils.getEffectIfActive;

import java.util.Optional;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEffectIsActive;
import bassebombecraft.operator.conditional.IfWorldAtServerSide;
import bassebombecraft.operator.event.ReflectMobDamageAmplified;
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
	 * Operator execution.
	 */
	static Operators ops;

	static {
		ops = new Operators();
		Operator reflectOp = new ReflectMobDamageAmplified(ops.getSplLivingDamageEvent(), ops.getSplEffectInstance());
		Operator ifOp2 = new IfEffectIsActive(ops.getSplLivingEntity(), reflectOp, REFLECT_EFFECT);
		Operator ifOp = new IfWorldAtServerSide(ops.getSplLivingEntity(), ifOp2); ;				
		ops.setOperator(ifOp);
	}

	@SubscribeEvent
	public static void handleLivingDamageEvent(LivingDamageEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Optional<EffectInstance> optEffect = getEffectIfActive(livingEntity, REFLECT_EFFECT);
		ops.run(event, event.getEntityLiving(), optEffect.orElse(NO_EFFECT));
	}

}
