package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnGuardianBook;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnGuardian;

/**
 * Book of guardian implementation.
 */
public class SpawnGuardianBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnGuardianBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnGuardian();

	public SpawnGuardianBook() {
		super( spawnGuardianBook, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
