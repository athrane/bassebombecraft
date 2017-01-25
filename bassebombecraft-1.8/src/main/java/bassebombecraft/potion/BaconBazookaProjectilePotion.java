package bassebombecraft.potion;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.List;

import com.typesafe.config.Config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BaconBazookaProjectilePotion extends Potion {

	/**
	 * Projectile force
	 */
	final int force;
	
	/**
	 * Projectile damage.
	 */
	final float damage;

	/**
	 * Projectile impact explosion
	 */
	final int explosion;	

	/**
	 * BaconBazookaPotion constructor.
	 * 
	 * @param isBadEffectIn
	 * @param liquidColorIn
	 */
	public BaconBazookaProjectilePotion(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
		
		Config configuration = getBassebombeCraft().getConfiguration();		
		force = configuration.getInt("BaconBazookaProjectilePotion.Force");
		damage = configuration.getInt("BaconBazookaProjectilePotion.Damage");
		explosion = configuration.getInt("BaconBazookaProjectilePotion.Explosion");		
	}

	@Override
	public void performEffect(EntityLivingBase entity, int magicNumber) {
			
		if (entity == null)
			return;

		// get look vector
		Vec3d lookVec = entity.getLookVec();

		// move entity i view direction
		double x = lookVec.xCoord * force;
		double y = lookVec.yCoord * force;
		double z = lookVec.zCoord * force;
		entity.move(MoverType.SELF, x, y, z);

		// get hit entities
		World world = entity.getEntityWorld();		
		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		
		// damage hit entities
		for(EntityLivingBase hitEntity : entities) {
			hitEntity.attackEntityFrom(DamageSource.GENERIC, damage);
		}

		// explode at the end of duration
		if(entity.deathTime > 18) {
	        world.createExplosion(entity, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), explosion, true);
		}

	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
}
