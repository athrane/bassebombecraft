package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class shoots an egg at nearby entities.
 */
public class ShootMeteor implements InventoryItemActionStrategy {

	static final float PITCH_OFFSET = 0.0F;
	static final SoundEvent SOUND = SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
	static Random random = new Random();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect range.
	 */
	int range;

	/**
	 * AddLevitationEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public ShootMeteor(String key) {
		infos = createFromConfig(key);
		Config configuration = getBassebombeCraft().getConfiguration();
		range = configuration.getInt(key + ".Range");

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

		double posX = target.posX + (random.nextInt(10) - 5);
		double posY = invoker.posY + 20 + (random.nextInt(10) - 5);
		double posZ = target.posZ + (random.nextInt(10) - 5);

		double d0 = target.posX - posX;
		double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - posY;
		double d2 = target.posZ - posZ;

		FireballEntity projectile = new FireballEntity(world, posX, posY, posZ, d0, d1, d2);

		invoker.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);
		world.addEntity(projectile);
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
