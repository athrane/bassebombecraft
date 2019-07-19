package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.potion.MobEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a saturation effect to the
 * invoker.
 */
public class AddSaturationEffect implements InventoryItemActionStrategy {

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * AddLevitationEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public AddSaturationEffect(String key) {
		infos = createFromConfig(key);
		Config configuration = getBassebombeCraft().getConfiguration();
		duration = configuration.getInt(key + ".Duration");
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
		if (target instanceof LivingEntity) {
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addPotionEffect(createEffect());
		}
	}

	@Override
	public int getEffectRange() {
		return 1; // Not a AOE effect
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
	PotionEffect createEffect() {
		return new PotionEffect(MobEffects.SATURATION, duration);
	}

}
