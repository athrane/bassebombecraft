package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import java.util.function.Supplier;

import bassebombecraft.ModConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

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
	 * AddMobsPrimingEffect constructor
	 * 
	 * @param splDuration duration as a potion effect.
	 */
	public AddMobsPrimingEffect(Supplier<Integer> splDuration) {
		duration = splDuration.get();
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
		return new EffectInstance(ModConstants.MOB_PRIMING_EFFECT, duration);
	}

}
