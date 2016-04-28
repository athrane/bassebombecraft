package bassebombecraft.item;

import bassebombecraft.structure.ExplosionStaffStructureFactory;

/**
 * Explosion staff. 
 */
public class ExplosionStaff extends GenericStaff {

	public final static String ITEM_NAME = "ExplosionStaff";
	
	public ExplosionStaff() {
		super(ITEM_NAME, new ExplosionStaffStructureFactory());
	}
		
}
