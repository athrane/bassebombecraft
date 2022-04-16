package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addBlindingEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addBlindingEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class blinds entities within range .
 */
public class AddBlindingEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddBlindingEffect.class.getSimpleName();

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
	public AddBlindingEffect() {
		duration = addBlindingEffectDuration.get();
		amplifier = addBlindingEffectAmplifier.get();
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
			entityLivingBase.addEffect(createEffect2());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(MobEffects.BLINDNESS, duration, amplifier);
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect2() {
		return new MobEffectInstance(MobEffects.NIGHT_VISION, duration, amplifier);
	}

}
