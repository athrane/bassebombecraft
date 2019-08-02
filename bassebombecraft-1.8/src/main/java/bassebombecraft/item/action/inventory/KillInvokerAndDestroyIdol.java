package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ConfigUtils.createFromConfig;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class kill the invoker and destroys the idol.
 */
public class KillInvokerAndDestroyIdol implements InventoryItemActionStrategy {

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * SpawnRain constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public KillInvokerAndDestroyIdol(String key) {
		infos = createFromConfig(key);
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

		// destroy idol
		Iterable<ItemStack> heldEquipment = invoker.getHeldEquipment();

		for (ItemStack equipment : heldEquipment) {
			int damage = equipment.getMaxDamage();
			equipment.setDamage(damage);
		}

		// kill target
		invoker.onKillCommand();
	}

	@Override
	public int getEffectRange() {
		return 1; // Not a AOE effect
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

}
