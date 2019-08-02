package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class shoots an egg at nearby entities.
 */
public class ShootEgg implements InventoryItemActionStrategy {

	static final float PITCH_OFFSET = 0.0F;
	static final SoundEvent SOUND = SoundEvents.ENTITY_CHICKEN_EGG;
	static Random random = new Random();

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
	 * @param key
	 *            configuration key to initialize particle rendering info from.
	 */
	public ShootEgg(String key) {
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

		EntityEgg projectile = new EntityEgg(world, invoker);
		
		// from EntityLlama.spit()
        double d0 = target.posX - invoker.posX;
        double d1 = target.getBoundingBox().minY + (double)(target.height / 3.0F) - projectile.posY;
        double d2 = target.posZ - invoker.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        projectile.shoot(d0, d1 + (double)f, d2, velocity, inaccuracy);
				
		invoker.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);
		world.spawnEntity(projectile);
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
