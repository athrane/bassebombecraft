package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.AGGRO_PLAYER_EFFECT;
import static bassebombecraft.config.ModConfiguration.addAggroPlayerEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.addAggroPlayerEffectDuration;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a aggro player effect to
 * nearby entities.
 */
public class AddAggroPlayerEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = AddAggroPlayerEffect.class.getSimpleName();

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
	public AddAggroPlayerEffect() {
		duration = addAggroPlayerEffectDuration.get();
		amplifier = addAggroPlayerEffectAmplifier.get();
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
		return new EffectInstance(AGGRO_PLAYER_EFFECT, duration, amplifier);
	}

}
