package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnLightningBolt;

/**
 * Book of lightning bolt implementation.
 */
public class LightningBoltBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = LightningBoltBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnLightningBolt();

	public LightningBoltBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
