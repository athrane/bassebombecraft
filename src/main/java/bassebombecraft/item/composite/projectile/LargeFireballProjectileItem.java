package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.largeFireballProjectileEntity;
import static bassebombecraft.config.ModConfiguration.largeFireballProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootLargeFireballProjectile2;

/**
 * Large fireball projectile item.
 */
public class LargeFireballProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LargeFireballProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LargeFireballProjectileItem() {
		super(largeFireballProjectileItem, largeFireballProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootLargeFireballProjectile2();
	}

}
