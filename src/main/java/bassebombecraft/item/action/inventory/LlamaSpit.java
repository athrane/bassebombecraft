package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spits at nearby entities.
 */
public class LlamaSpit implements InventoryItemActionStrategy {

	/**
	 * Spit spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.LLAMA_SPIT;

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
		Random random = getBassebombeCraft().getRandom();
		net.minecraft.world.entity.projectile.LlamaSpit entity = EntityType.LLAMA_SPIT.create(world);

		// from EntityLlama.spit()
		double d0 = target.getX() - invoker.getX();
		double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - entity.getY();
		double d2 = target.getZ() - invoker.getZ();
		float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
		entity.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
		world.playSound((Player) null, invoker.getX(), invoker.getY(), invoker.getZ(), SoundEvents.LLAMA_SPIT,
				invoker.getSoundSource(), 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
		world.addFreshEntity(entity);
	}

}
