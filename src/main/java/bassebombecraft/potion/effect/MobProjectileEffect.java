package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.ModConstants.POTION_MOB_DEATH_TIME_TRIGGER;
import static bassebombecraft.entity.EntityUtils.explode;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Effect which shoots a mob along its view vector.
 */
public class MobProjectileEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = MobProjectileEffect.class.getSimpleName();

	/**
	 * Explosion will make smoke.
	 */
	static final boolean IS_SMOKING = true;

	/**
	 * Function to get projectile force.
	 */
	Supplier<Integer> splForce;

	/**
	 * Function to get projectile impact explosion size.
	 */
	Supplier<Integer> splExplosion;

	/**
	 * Constructor.
	 * 
	 * @param splForce     function to get projectile force.
	 * @param splExplosion function to get explosion size.
	 */
	public MobProjectileEffect(Supplier<Integer> splForce, Supplier<Integer> splExplosion) {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		this.splForce = splForce;
		this.splExplosion = splExplosion;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

		if (entity == null)
			return;

		// get look vector
		Vec3 lookVec = entity.getLookAngle();

		// move entity i view direction
		int force = splForce.get();
		double x = lookVec.x * force;
		double y = lookVec.y * force;
		double z = lookVec.z * force;
		Vec3 moveVec = new Vec3(x, y, z);
		entity.move(MoverType.SELF, moveVec);

		// get hit entities
		Level world = entity.getCommandSenderWorld();
		AABB aabb = entity.getBoundingBox();
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb);

		splExplosion.get();

		// explosion if any entities where hit
		if (!entities.isEmpty()) {

			for (LivingEntity hitEntity : entities) {

				// skip entity itself
				if (hitEntity.equals(entity))
					continue;

				// trigger explosion
				explode(entity, world, splExplosion.get());

				// kill projectile mob
				DamageSource cause = DamageSource.MAGIC;
				entity.die(cause);

				return;
			}

		}

		// explode at the end of duration
		if (entity.deathTime > POTION_MOB_DEATH_TIME_TRIGGER) {
			explode(entity, world, splExplosion.get());
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

}
