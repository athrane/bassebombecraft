package bassebombecraft.util.function;

import static bassebombecraft.entity.EntityUtils.isType;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.function.Function;
import java.util.function.Predicate;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;

/**
 * Reusable predicates.
 */
public class Predicates {

	/**
	 * Null predicate which always returns true.
	 */
	static final Predicate<Entity> NULL_PREDICATE_ENTITY = p -> true;

	/**
	 * Null predicate which always returns true.
	 */
	static final Predicate<LivingEntity> NULL_PREDICATE_LIVINGENTITY = p -> true;	

	/**
	 * Function which returns a null predicate (it always returns true).
	 * 
	 * @return function which returns a null predicate (it always returns true).
	 */
	public static Function<Ports, Predicate<Entity>> fnGetNullPredicateForEntity() {
		return p -> NULL_PREDICATE_ENTITY;
	}

	/**
	 * Function which returns a null predicate (it always returns true).
	 * 
	 * @return function which returns a null predicate (it always returns true).
	 */
	public static Function<Ports, Predicate<LivingEntity>> fnGetNullPredicateForLivingEntity() {
		return p -> NULL_PREDICATE_LIVINGENTITY;
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if both entities has
	 * identical same IDs.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if both entities has
	 *         identical IDs.
	 */
	public static Predicate<Entity> hasIdenticalIds(Entity e1) {
		return e2 -> hasIdenticalUniqueID(e1, e2);
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if compared
	 * {@linkplain Entity} has different IDs.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if compared
	 *         {@linkplain Entity} has different IDs.
	 */
	public static Predicate<Entity> hasDifferentIds(Entity e1) {
		return e2 -> !hasIdenticalUniqueID(e1, e2);
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if compared
	 * {@linkplain Entity} and {@linkplain LivingEntity}has different IDs.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if compared
	 *         {@linkplain Entity} has different IDs.
	 */
	public static Predicate<LivingEntity> hasLivingEntitiesDifferentIds(Entity e1) {
		return e2 -> !hasIdenticalUniqueID(e1, e2);
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if entity #1 isn't a
	 * {@linkplain GenericCompositeProjectileEntity} AND the projectile shooter
	 * hasn't the same ID as entity #2.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if if entity #1 isn't a
	 *         {@linkplain GenericCompositeProjectileEntity} AND the projectile
	 *         shooter hasn't the same ID as entity #2.
	 */
	public static Predicate<Entity> isntProjectileShooter(Entity e1) {
		return e2 -> {

			// exit if e1 isn't a composite projectile
			if (!isType(e1, GenericCompositeProjectileEntity.class))
				return true;

			GenericCompositeProjectileEntity projectile = (GenericCompositeProjectileEntity) e1;
			Entity shooter = projectile.getOwner();
			return !hasIdenticalUniqueID(shooter, e2);
		};
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if
	 * {@linkplain LivingEntity} has active potion effect.
	 * 
	 * @param effect potion effect to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if
	 *         {@linkplain LivingEntity} has active potion effect.
	 */
	public static Predicate<LivingEntity> hasPotionEffect(MobEffect effect) {
		return e -> e.hasEffect(effect);
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if
	 * {@linkplain LivingEntity} doesn't has active potion effect.
	 * 
	 * @param effect potion effect to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if
	 *         {@linkplain LivingEntity} has active potion effect.
	 */
	public static Predicate<LivingEntity> hasNotPotionEffect(MobEffect effect) {
		return e -> !(e.hasEffect(effect));
	}
	
}
