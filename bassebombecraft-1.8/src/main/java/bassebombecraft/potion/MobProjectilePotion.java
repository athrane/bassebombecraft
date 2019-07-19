package bassebombecraft.potion;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.ModConstants.POTION_MOB_DEATH_TIME_TRIGGER;
import static bassebombecraft.entity.EntityUtils.explode;

import java.util.List;

import com.typesafe.config.Config;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Potion which shoots a mob along its view vector.
 */
public class MobProjectilePotion extends Potion {

	/**
	 * Explosion will make smoke.
	 */
	static final boolean IS_SMOKING = true;

	/**
	 * Projectile force
	 */
	final int force;

	/**
	 * Projectile impact explosion size.
	 */
	final int explosion;

	/**
	 * MobProjectilePotion constructor.
	 * 
	 * @param key
	 *            configuration key.
	 */
	public MobProjectilePotion(String key) {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);

		Config configuration = getBassebombeCraft().getConfiguration();
		force = configuration.getInt(key + ".Force");
		explosion = configuration.getInt(key + ".Explosion");
	}

	@Override
	public void performEffect(LivingEntity entity, int magicNumber) {

		if (entity == null)
			return;

		// get look vector
		Vec3d lookVec = entity.getLookVec();

		// move entity i view direction
		double x = lookVec.x * force;
		double y = lookVec.y * force;
		double z = lookVec.z * force;
		entity.move(MoverType.SELF, x, y, z);

		// get hit entities
		World world = entity.getEntityWorld();
		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		// explosion if any entities where hit
		if (!entities.isEmpty()) {
			
			for(LivingEntity hitEntity : entities) {				

				// skip entity itself
				if(hitEntity.equals(entity)) continue;
				
				// trigger explosion
				explode(entity, world, explosion);
				
				// kill projectile mob
				entity.setDead();
				
				return;				
			}
			
		}

		// explode at the end of duration
		if (entity.deathTime > POTION_MOB_DEATH_TIME_TRIGGER) {
			explode(entity, world, explosion);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
