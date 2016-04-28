package bassebombecraft.tab;

import net.minecraft.creativetab.CreativeTabs;

/**
 * Factory for creation of creative tabs.
 */
public class CreativeTabFactory {

	/**
	 * Create creative tab with next available tab ID.
	 * 
	 * @param name tab name.
	 * 
	 * @return creative tab.
	 */
	public static CreativeTabs createCreativeTab(String name) {
		return new BassebombeTab(CreativeTabs.getNextID(), name);
	}
}
