package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.MOB_AGGRO_EFFECT;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import java.util.function.Supplier;

import javax.naming.OperationNotSupportedException;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a mobs aggro effect to
 * nearby entities.
 */
public class AddMobsAggroEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public final static String NAME = AddMobsAggroEffect.class.getSimpleName();

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * AddMobsAggroEffect constructor
	 * 
	 * @param splDuration  duration as a potion effect.
	 * @param splAmplifier amplifier as a potion effect.
	 */
	public AddMobsAggroEffect(Supplier<Integer> splDuration, Supplier<Integer> splAmplifier) {
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
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(MOB_AGGRO_EFFECT, duration, amplifier);
	}

	@Override
	public int getEffectRange() throws OperationNotSupportedException {
		throw new OperationNotSupportedException(); // to signal that this method should not be used.
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() throws OperationNotSupportedException {
		throw new OperationNotSupportedException(); // to signal that this method should not be used.
	}

}
