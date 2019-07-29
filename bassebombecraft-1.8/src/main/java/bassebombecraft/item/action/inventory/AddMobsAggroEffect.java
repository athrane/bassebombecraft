package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.potion.MobEffects;
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
	 * AddMobsAggroEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public AddMobsAggroEffect(String key) {
		infos = createFromConfig(key);
		Config configuration = getBassebombeCraft().getConfiguration();
		duration = configuration.getInt(key + ".Duration");
		range = configuration.getInt(key + ".Range");
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
		if (target instanceof LivingEntity) {
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
		return new EffectInstance(MobEffects.MOBS_AGGRO_POTION, duration);
	}

}
