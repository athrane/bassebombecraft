package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.smallFireballProjectileEntity;
import static bassebombecraft.config.ModConfiguration.smallFireballProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;

/**
 * Small fireball projectile item.
 */
public class SmallFireballProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = SmallFireballProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public SmallFireballProjectileItem() {
		super(smallFireballProjectileItem, smallFireballProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootFireballProjectile2();
	}

}
