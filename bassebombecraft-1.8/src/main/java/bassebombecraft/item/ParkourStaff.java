package bassebombecraft.item;

import bassebombecraft.structure.ParkourStaffStructureFactory;

/**
 * Staff of parkour. 
 */
public class ParkourStaff extends GenericStaff {

	public final static String ITEM_NAME = "ParkourStaff";
	
	public ParkourStaff() {
		super(ITEM_NAME, new ParkourStaffStructureFactory());
	}
		
}
