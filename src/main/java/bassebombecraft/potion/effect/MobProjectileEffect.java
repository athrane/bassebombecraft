package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.ModConstants.POTION_MOB_DEATH_TIME_TRIGGER;
import static bassebombecraft.entity.EntityUtils.explode;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Effect which shoots a mob along its view vector.
 */
public class MobProjectileEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = MobProjectileEffect.class.getSimpleName();

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
	 * @param force     projectile force.
	 * @param explosion explosion size.
	 */
	public MobProjectileEffect(int force, int explosion) {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		this.force = force;
		this.explosion = explosion;
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		if (entity == null)
			return;

		// get look vector
		Vector3d lookVec = entity.getLookVec();

		// move entity i view direction
		double x = lookVec.x * force;
		double y = lookVec.y * force;
		double z = lookVec.z * force;
		Vector3d moveVec = new Vector3d(x, y, z);
		entity.move(MoverType.SELF, moveVec);

		// get hit entities
		World world = entity.getEntityWorld();
		AxisAlignedBB aabb = entity.getBoundingBox();
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		// explosion if any entities where hit
		if (!entities.isEmpty()) {

			for (LivingEntity hitEntity : entities) {

				// skip entity itself
				if (hitEntity.equals(entity))
					continue;

				// trigger explosion
				explode(entity, world, explosion);

				// kill projectile mob
				DamageSource cause = DamageSource.MAGIC;
				entity.onDeath(cause);

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
