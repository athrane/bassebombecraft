package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.NOT_AN_AOE_EFFECT;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import bassebombecraft.config.InventoryItemConfig;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes it rain.
 */
public class SpawnRain implements InventoryItemActionStrategy {

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * SpawnRain constructor.
	 * 
	 * @param config inventory item configuration.
	 */
	public SpawnRain(InventoryItemConfig config) {
		infos = createFromConfig(config.particles);
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
		world.getWorldInfo().setRaining(true);
	}

	@Override
	public int getEffectRange() {
		return NOT_AN_AOE_EFFECT;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

}
