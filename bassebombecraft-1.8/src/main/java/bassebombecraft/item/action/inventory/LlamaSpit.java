package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spits at nearby entities.
 */
public class LlamaSpit implements InventoryItemActionStrategy {

	static final SoundEvent SOUND = SoundEvents.ENTITY_LLAMA_SPIT;

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
	 * Projectile velocity.
	 */
	int velocity;

	/**
	 * Projectile inaccuracy.
	 */
	int inaccuracy;

	/**
	 * AddLevitationEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public LlamaSpit(String key) {
		infos = createFromConfig(key);
		Config configuration = getBassebombeCraft().getConfiguration();
		range = configuration.getInt(key + ".Range");
		velocity = configuration.getInt(key + ".Velocity");
		inaccuracy = configuration.getInt(key + ".Inaccuracy");
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
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		Vec3d v3 = invoker.getLook(1);
		Random random = getBassebombeCraft().getRandom();

		LlamaSpitEntity entity = EntityType.LLAMA_SPIT.create(world);
		// EntityLlamaSpit projectile = new EntityLlamaSpit(world, invoker.posX,
		// invoker.posY + invoker.getEyeHeight(), invoker.posZ, v3.x, v3.y, v3.z);

		// from EntityLlama.spit()
		double d0 = target.posX - invoker.posX;
		double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - entity.posY;
		double d2 = target.posZ - invoker.posZ;
		float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
		entity.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
		world.playSound((PlayerEntity) null, invoker.posX, invoker.posY, invoker.posZ, SoundEvents.ENTITY_LLAMA_SPIT,
				invoker.getSoundCategory(), 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
		world.addEntity(entity);
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
