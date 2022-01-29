package bassebombecraft.util.function;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Reusable functions.
 */
public class Functions {

	/**
	 * Cast {@linkplain LivingEntity} to {@linkplain Entity}.
	 * 
	 * @return return {@linkplain Entity}.
	 * 
	 * @throws ClassCastException if the object is not null and is not assignable to
	 *                            {@linkplain Entity}.
	 */
	public static Function<LivingEntity, Entity> getFnCastToEntity() {
		return le -> Entity.class.cast(le);
	}

}
