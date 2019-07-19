package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spawns a entities within range om fire.
 */
public class AddFlameEffect implements InventoryItemActionStrategy {

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
	 * AddFlameEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public AddFlameEffect(String key) {
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
	public void applyEffect(Entity target, World world, LivingEntity sinvoker) {
		target.setFire(duration);
	}

	@Override
	public int getEffectRange() {
		return range;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

}
