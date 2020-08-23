package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.lightningProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootLightningProjectile2;

/**
 * Lightning projectile item.
 */
public class LightningProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LightningProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LightningProjectileItem() {
		super(NAME, lightningProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootLightningProjectile2();
	}

}
