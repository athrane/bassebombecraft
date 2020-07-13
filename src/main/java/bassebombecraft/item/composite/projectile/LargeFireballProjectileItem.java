package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.largeFireballProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.LargeFireballProjectile2;

/**
 * Large fireball projectile item.
 */
public class LargeFireballProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LargeFireballProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LargeFireballProjectileItem() {
		super(NAME, largeFireballProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new LargeFireballProjectile2();
	}

}
