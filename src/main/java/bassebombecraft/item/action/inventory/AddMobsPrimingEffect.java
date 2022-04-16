package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addMobsPrimingEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.potion.effect.RegisteredEffects.MOB_PRIMING_EFFECT;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a priming effect to nearby
 * entities.
 */
public class AddMobsPrimingEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddMobsPrimingEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor
	 */
	public AddMobsPrimingEffect() {
		duration = addMobsPrimingEffectDuration.get();
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
		return new MobEffectInstance(MOB_PRIMING_EFFECT.get(), duration);
	}

}
