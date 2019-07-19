package bassebombecraft.predicate;

import com.google.common.base.Predicate;

import net.minecraft.entity.LivingEntity;

/**
 * Discard self filter.
 */
public class DiscardSelf implements Predicate<LivingEntity> {
	LivingEntity entity;

	public void set(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public boolean apply(LivingEntity otherEntity) {
		if (this.entity == null)
			return false;
		if (otherEntity == null)
			return false;
		
		return (!entity.equals(otherEntity));
	}

}