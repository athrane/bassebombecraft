package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class charms a beast.
 */
public class CharmBeast implements InventoryItemActionStrategy {

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
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		try {
			
			// skip if entity can't be charmed, i.e. is a mob entity
			if (!isTypeMobEntity(target))
				return;

			// type cast
			Mob mobEntity = (Mob) target;

			// register mob as charmed
			getProxy().getServerCharmedMobsRepository().add(mobEntity, invoker);
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}

}
