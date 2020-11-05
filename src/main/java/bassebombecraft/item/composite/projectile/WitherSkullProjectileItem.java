package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.skullProjectileEntity;
import static bassebombecraft.config.ModConfiguration.witherSkullProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootWitherSkullProjectile2;

/**
 * Wither skull projectile item.
 */
public class WitherSkullProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = WitherSkullProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public WitherSkullProjectileItem() {
		super(witherSkullProjectileItem, skullProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootWitherSkullProjectile2();
	}

}
