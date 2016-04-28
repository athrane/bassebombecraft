package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.EmitVerticalForce;
import bassebombecraft.projectile.action.ProjectileAction;

/**
 * Book of emit vertical force implementation.
 */
public class EmitVerticalForceBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "EmitVerticalForceBook";
	static final ProjectileAction PROJECTILE_ACTION = new EmitVerticalForce();

	public EmitVerticalForceBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
