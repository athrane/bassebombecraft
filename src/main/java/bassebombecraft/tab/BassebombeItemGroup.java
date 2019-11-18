package bassebombecraft.tab;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * 
 * {@linkplain ItemGroup} implementation, which defines creative tab.
 */
public class BassebombeItemGroup extends ItemGroup {

	public BassebombeItemGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		Item[] items = getBassebombeCraft().getInventoryItems();
		Random random = getBassebombeCraft().getRandom();
		int index = random.nextInt(items.length);
		Item item = items[index];
		return new ItemStack(item);
	}

}
