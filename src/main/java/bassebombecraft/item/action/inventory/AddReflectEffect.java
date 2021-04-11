package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.REFLECT_EFFECT;
import static bassebombecraft.config.ModConfiguration.addReflectEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addReflectEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

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
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLiving = (LivingEntity) target;
			entityLiving.addPotionEffect(createEffect());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(REFLECT_EFFECT, duration, amplifier);
	}

}
