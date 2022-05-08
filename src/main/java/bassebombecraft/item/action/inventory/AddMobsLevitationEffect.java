package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addMobsLevitationEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addMobsLevitationEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static net.minecraft.world.effect.MobEffects.LEVITATION;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a levitation effect to
 * nearby entities.
 */
public class AddMobsLevitationEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddMobsLevitationEffect.class.getSimpleName();

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
	public AddMobsLevitationEffect() {
		duration = addMobsLevitationEffectDuration.get();
		amplifier = addMobsLevitationEffectAmplifier.get();
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
		return new MobEffectInstance(LEVITATION, duration, amplifier);
	}

}
