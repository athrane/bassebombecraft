package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.eggProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.NullOp2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;

/**
 * Egg projectile item.
 */
public class EggProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = EggProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public EggProjectileItem() {
		super(NAME, eggProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootOperatorEggProjectile2(new NullOp2());
	}

}
