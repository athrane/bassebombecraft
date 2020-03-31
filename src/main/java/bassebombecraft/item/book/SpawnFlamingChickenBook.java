package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnFlamingChickenBook;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnFlamingChicken;

/**
 * Book of failed phoenix .
 */
public class SpawnFlamingChickenBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnFlamingChickenBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnFlamingChicken();

	public SpawnFlamingChickenBook() {
		super(ITEM_NAME, spawnFlamingChickenBook, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
