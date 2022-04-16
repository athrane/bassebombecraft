package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.killEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {

		// destroy idol
		Iterable<ItemStack> heldEquipment = invoker.getHandSlots();

		for (ItemStack equipment : heldEquipment) {
			int damage = equipment.getMaxDamage();
			equipment.setDamageValue(damage);
		}

		// kill target
		killEntity(invoker);
	}

}
