package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addReflectEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addReflectEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import bassebombecraft.potion.effect.RegisteredEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a reflect effect to the
 * invoker.
 */
public class AddReflectEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddReflectEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * Constructor
	 */
	public AddReflectEffect() {
		duration = addReflectEffectDuration.get();
		amplifier = addReflectEffectAmplifier.get();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return targetIsInvoker;
	}

	@Override
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLiving = (LivingEntity) target;
			entityLiving.addEffect(createEffect());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(RegisteredEffects.REFLECT_EFFECT.get(), duration, amplifier);
	}

}
