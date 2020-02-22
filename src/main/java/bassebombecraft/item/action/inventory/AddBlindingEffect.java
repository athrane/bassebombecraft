package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

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
	 * AddBlindingEffect constructor
	 * 
	 * @param splDuration  duration as a potion effect.
	 * @param splAmplifier amplifier as a potion effect.
	 */
	public AddBlindingEffect(Supplier<Integer> splDuration, Supplier<Integer> splAmplifier) {
		duration = splDuration.get();
		amplifier = splAmplifier.get();
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
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addPotionEffect(createEffect());
			entityLivingBase.addPotionEffect(createEffect2());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(Effects.BLINDNESS, duration, amplifier);
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect2() {
		return new EffectInstance(Effects.NIGHT_VISION, duration, amplifier);
	}

}
