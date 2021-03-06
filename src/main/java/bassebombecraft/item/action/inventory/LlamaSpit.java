package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spits at nearby entities.
 */
public class LlamaSpit implements InventoryItemActionStrategy {

	/**
	 * Spit spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_LLAMA_SPIT;

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
		Random random = getBassebombeCraft().getRandom();
		LlamaSpitEntity entity = EntityType.LLAMA_SPIT.create(world);

		// from EntityLlama.spit()
		double d0 = target.getPosX() - invoker.getPosX();
		double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - entity.getPosY();
		double d2 = target.getPosZ() - invoker.getPosZ();
		float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
		entity.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
		world.playSound((PlayerEntity) null, invoker.getPosX(), invoker.getPosY(), invoker.getPosZ(), SoundEvents.ENTITY_LLAMA_SPIT,
				invoker.getSoundCategory(), 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
		world.addEntity(entity);
	}

}
