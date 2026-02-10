package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class transform an entity to a chicken.
 */
public class Chickenize implements InventoryItemActionStrategy {

	/**
	 * Entity initial age.
	 */
	static final int CHILD_AGE = 0;

	/**
	 * Entity spawn sound.
	 */
	SoundEvent SOUND = SoundEvents.CHICKEN_HURT;

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		if (target instanceof Chicken)
			return false;
		return true;
	}

	@Override
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {

		// get entity position
		BlockPos position = target.blockPosition();
		float yaw = target.getYRot();
		float pitch = target.getXRot();

		// kill of entity
		target.remove(Entity.RemovalReason.DISCARDED);

		// spawn chicken
		Chicken entity = EntityType.CHICKEN.create(world);
		entity.setAge(CHILD_AGE);
		entity.moveTo(position.getX(), position.getY(), position.getZ(), yaw, pitch);
		world.addFreshEntity(entity);

		// play sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	}

}
