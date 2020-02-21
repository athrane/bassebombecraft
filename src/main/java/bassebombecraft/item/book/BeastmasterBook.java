package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.CharmEntity;
import bassebombecraft.projectile.action.ProjectileAction;

/**
 * Book of beast master. 
 */
public class BeastmasterBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "BeastmasterBook";
	static final ProjectileAction PROJECTILE_ACTION = new CharmEntity();
	
	public BeastmasterBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
