package bassebombecraft.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Basic creative tab with a skull as icon.
 */
class BassebombeTab extends CreativeTabs{

	public BassebombeTab(int id, String name) {
		super(id, name);
	}

	public BassebombeTab(String name) {
		super(name);
	}

	@Override
	public Item getTabIconItem() {
		return Items.skull;
	}


}
