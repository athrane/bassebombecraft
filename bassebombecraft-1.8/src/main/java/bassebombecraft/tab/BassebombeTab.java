package bassebombecraft.tab;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Basic creative tab.
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
		Item[] items = getBassebombeCraft().getInventoryItems();
		Random random = new Random();
		int index = random.nextInt(items.length);
		Item item = items[index];
		return new ItemStack(item);
	}

}
