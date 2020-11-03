package bassebombecraft.tab;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Sub class of {@linkplain ItemGroup} which defines creative tab.
 */
public class BassebombeItemGroup extends ItemGroup {

	public BassebombeItemGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(getBassebombeCraft().getCreativeTabItem());
	}

}
