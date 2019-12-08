package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnGuardianBook;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnGuardian;

/**
 * Book of guardian implementation.
 */
public class SpawnGuardianBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnGuardianBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnGuardian();

	public SpawnGuardianBook() {
		super(ITEM_NAME, spawnGuardianBook, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
