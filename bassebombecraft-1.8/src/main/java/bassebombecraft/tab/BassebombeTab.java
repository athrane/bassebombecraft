package bassebombecraft.tab;

import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
	public ItemStack getTabIconItem() {
		return new ItemStack(new LightningBoltIdolInventoryItem());
	}

}
