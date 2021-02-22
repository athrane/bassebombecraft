package bassebombecraft.util.function;

import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.function.Function;
import java.util.function.Predicate;

import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;

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

}
