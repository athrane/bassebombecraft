package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.EmitHorizontalForce;
import bassebombecraft.projectile.action.ProjectileAction;

/**
 * Book of emit horizontal force implementation.
 */
public class EmitHorizontalForceBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "EmitHorizontalForceBook";
	static final ProjectileAction PROJECTILE_ACTION = new EmitHorizontalForce();

	public EmitHorizontalForceBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
