package bassebombecraft.item.composite.projectile;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootWitherSkullProjectile2;

/**
 * Wither skull projectile item.
 */
public class WitherSkullProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = WitherSkullProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public WitherSkullProjectileItem() {
		super(NAME, ModConfiguration.decoyBook);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootWitherSkullProjectile2();
	}

}
