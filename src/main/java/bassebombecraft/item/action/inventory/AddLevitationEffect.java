package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.addLevitationEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addLevitationEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static net.minecraft.potion.Effects.LEVITATION;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a levitation effect to the
 * invoker.
 */
public class AddLevitationEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddLevitationEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * Constructor.
	 */
	public AddLevitationEffect() {
		duration = addLevitationEffectDuration.get();
		amplifier = addLevitationEffectAmplifier.get();
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
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addPotionEffect(createEffect());
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(LEVITATION, duration, amplifier);
	}

}
