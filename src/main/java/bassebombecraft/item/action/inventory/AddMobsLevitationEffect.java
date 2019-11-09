package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static net.minecraft.potion.Effects.LEVITATION;

import java.util.function.Supplier;

import bassebombecraft.config.InventoryItemConfig;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a levitation effect to
 * nearby entities..
 */
public class AddMobsLevitationEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public final static String NAME = AddMobsLevitationEffect.class.getSimpleName();
	
	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect range.
	 */
	int range;

	/**
	 * Effect amplifier.
	 */
	int amplification;

	/**
	 * AddMobsLevitationEffect constructor
	 * 
	 * @param config       inventory item configuration.
	 * @param splDuration  duration as a potion effect.
	 * @param splAmplifier amplifier as a potion effect.
	 */
	public AddMobsLevitationEffect(InventoryItemConfig config, Supplier<Integer> splDuration,
			Supplier<Integer> splAmplifier) {
		infos = createFromConfig(config.particles);
		duration = splDuration.get();
		amplification = splAmplifier.get();
		range = config.range.get();
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

	@Override
	public int getEffectRange() {
		return range;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(LEVITATION, duration, amplification);
	}

}
