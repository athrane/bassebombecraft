package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addSaturationEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a saturation effect to the
 * invoker.
 */
public class AddSaturationEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddSaturationEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor
	 */
	public AddSaturationEffect() {
		duration = addSaturationEffectDuration.get();
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
		return new MobEffectInstance(MobEffects.SATURATION, duration);
	}

}
