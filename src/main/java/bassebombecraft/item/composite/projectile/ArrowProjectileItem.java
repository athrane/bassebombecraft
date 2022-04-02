package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.arrowProjectileEntity;
import static bassebombecraft.config.ModConfiguration.arrowProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootArrowProjectile2;

/**
 * Arrow projectile item.
 */
public class ArrowProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = ArrowProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public ArrowProjectileItem() {
		super(arrowProjectileItem, arrowProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootArrowProjectile2();
	}

}
