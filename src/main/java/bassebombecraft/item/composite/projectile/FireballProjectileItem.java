package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.fireballProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;

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
		super(NAME, fireballProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootFireballProjectile2();
	}

}
