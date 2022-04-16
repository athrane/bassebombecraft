package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addAggroMobEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addAggroMobEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.potion.effect.RegisteredEffects.AGGRO_MOB_EFFECT;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a aggro mob effect to nearby
 * entities.
 */
public class AddAggroMobEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddAggroMobEffect.class.getSimpleName();

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
	public AddAggroMobEffect() {
		duration = addAggroMobEffectDuration.get();
		amplifier = addAggroMobEffectAmplifier.get();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addEffect(createEffect());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(AGGRO_MOB_EFFECT.get(), duration, amplifier);
	}

}
