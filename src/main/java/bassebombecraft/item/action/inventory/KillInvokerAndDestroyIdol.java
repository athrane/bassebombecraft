package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.killEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class kill the invoker and destroys the idol.
 */
public class KillInvokerAndDestroyIdol implements InventoryItemActionStrategy {

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return targetIsInvoker;
	}

	@Override
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {

		// destroy idol
		Iterable<ItemStack> heldEquipment = invoker.getHeldEquipment();

		for (ItemStack equipment : heldEquipment) {
			int damage = equipment.getMaxDamage();
			equipment.setDamage(damage);
		}

		// kill target
		killEntity(invoker);
	}

}
