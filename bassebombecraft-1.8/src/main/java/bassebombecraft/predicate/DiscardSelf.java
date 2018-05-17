package bassebombecraft.predicate;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;

/**
 * Discard self filter.
 */
public class DiscardSelf implements Predicate<EntityLivingBase> {
	EntityLivingBase entity;

	public void set(EntityLivingBase entity) {
		this.entity = entity;
	}

	@Override
	public boolean apply(EntityLivingBase otherEntity) {
		if (this.entity == null)
			return false;
		if (otherEntity == null)
			return false;
		
		return (!entity.equals(otherEntity));
	}

}