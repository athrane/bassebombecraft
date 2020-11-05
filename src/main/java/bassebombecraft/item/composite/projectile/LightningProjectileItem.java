package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.lightningProjectileEntity;
import static bassebombecraft.config.ModConfiguration.lightningProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootLightningProjectile2;

/**
 * Lightning projectile item.
 */
public class LightningProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LightningProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LightningProjectileItem() {
		super(lightningProjectileItem, lightningProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootLightningProjectile2();
	}

}
