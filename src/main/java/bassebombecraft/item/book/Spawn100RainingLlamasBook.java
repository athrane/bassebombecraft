package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.Spawn100RainingLlamas;

/**
 * Book of 100 raining llamas implementation.
 */
public class Spawn100RainingLlamasBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = Spawn100RainingLlamasBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new Spawn100RainingLlamas();

	public Spawn100RainingLlamasBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
