package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spits at nearby entities.
 */
public class LlamaSpit implements InventoryItemActionStrategy {

	static final SoundEvent SOUND = SoundEvents.ENTITY_LLAMA_SPIT;
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
	 * AddLevitationEffect constructor
	 * 
	 * @param key
	 *            configuration key to initialize particle rendering info from.
	 */
	public LlamaSpit(String key) {
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
	public void applyEffect(Entity target, World world, EntityLivingBase invoker) {
		Vec3d v3 = invoker.getLook(1);

		EntityLlamaSpit projectile = new EntityLlamaSpit(world, invoker.posX,
				invoker.posY + invoker.getEyeHeight(), invoker.posZ, v3.xCoord, v3.yCoord, v3.zCoord);
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
