package bassebombecraft.util.function;

import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.function.Function;
import java.util.function.Predicate;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Reusable predicates.
 */
public class Predicates {

	/**
	 * Null predicate which always returns true.
	 */
	static final Predicate<Entity> NULL_PREDICATE = p -> {
		return true;
	};

	/**
	 * Function which returns a null predicate (i.e. which always returns true).
	 */
	static Function<Ports, Predicate<Entity>> fnGetNullPredicate = p -> NULL_PREDICATE;

	/**
	 * Function which returns a null predicate (it always returns true).
	 * 
	 * @return function which returns a null predicate (it always returns true).
	 */
	public static Function<Ports, Predicate<Entity>> fnGetNullPredicate() {
		return fnGetNullPredicate;
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
		return (e2) -> {
			return hasIdenticalUniqueID(e1, e2);
		};
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if both entities has
	 * different IDs.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if both entities has
	 *         different IDs.
	 */
	public static Predicate<Entity> hasDifferentIds(Entity e1) {
		return (e2) -> {
			return !hasIdenticalUniqueID(e1, e2);
		};
	}

	/**
	 * Returns {@linkplain Predicate} which returns true if entity #1 isn't a
	 * {@linkplain GenericCompositeProjectileEntity} AND the projectile thrower
	 * hasn't the same ID as entity #2.
	 * 
	 * @param e1 entity to test.
	 * 
	 * @return {@linkplain Predicate} which returns true if if entity #1 isn't a
	 *         {@linkplain GenericCompositeProjectileEntity} AND the projectile
	 *         thrower hasn't the same ID as entity #2.
	 */
	public static Predicate<Entity> isntProjectileThrower(Entity e1) {
		return (e2) -> {

			// exit if e1 isn't a composite projectile
			if (!EntityUtils.isType(e1, GenericCompositeProjectileEntity.class))
				return true;

			GenericCompositeProjectileEntity projectile = (GenericCompositeProjectileEntity) e1;
			LivingEntity thrower = projectile.getThrower();
			return !hasIdenticalUniqueID(thrower, e2);
		};
	}

}
