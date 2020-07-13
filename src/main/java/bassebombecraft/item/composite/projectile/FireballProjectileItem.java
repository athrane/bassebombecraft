package bassebombecraft.item.composite.projectile;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.FireballProjectile2;

/**
 * Fireball projectile item.
 */
public class FireballProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = FireballProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public FireballProjectileItem() {
		super(NAME, ModConfiguration.fireballProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new FireballProjectile2();
	}

}
