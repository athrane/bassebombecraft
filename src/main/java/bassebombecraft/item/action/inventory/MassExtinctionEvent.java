package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.killEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class kill all entities and change the world
 * into a wasteland.
 */
public class MassExtinctionEvent implements InventoryItemActionStrategy {

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
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		killEntity(invoker);
	}

}
