package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.NOT_AN_AOE_EFFECT;
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
 * of inventory item actions. This class makes adds a levitation effect to the
 * invoker.
 */
public class AddLevitationEffect implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public final static String NAME = AddLevitationEffect.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * AddLevitationEffect constructor
	 * 
	 * @param config       inventory item configuration.
	 * @param splDuration  duration as a potion effect.
	 * @param splAmplifier amplifier as a potion effect.
	 */
	public AddLevitationEffect(InventoryItemConfig config, Supplier<Integer> splDuration,
			Supplier<Integer> splAmplifier) {
		infos = createFromConfig(config.particles);
		duration = splDuration.get();
		amplifier = splAmplifier.get();
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
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addPotionEffect(createEffect());
		}
	}

	@Override
	public int getEffectRange() {
		return NOT_AN_AOE_EFFECT;
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
		return new EffectInstance(LEVITATION, duration, amplifier);
	}

}
