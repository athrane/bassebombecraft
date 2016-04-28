package bassebombecraft.item;

import bassebombecraft.structure.GardenStructureFactory;

/**
 * Garden staff. 
 */
public class GardenStaff extends GenericStaff {

	public final static String ITEM_NAME = "GardenStaff";
	
	public GardenStaff() {
		super(ITEM_NAME, new GardenStructureFactory());
	}
		
}
