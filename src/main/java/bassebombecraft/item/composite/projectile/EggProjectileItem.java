package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.eggProjectileEntity;
import static bassebombecraft.config.ModConfiguration.eggProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootEggProjectile2;

/**
 * Egg projectile item.
 */
public class EggProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = EggProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public EggProjectileItem() {
		super(eggProjectileItem, eggProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootEggProjectile2();
	}

}
