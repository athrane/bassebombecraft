package bassebombecraft.tab;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Sub class of {@linkplain ItemGroup} which defines creative tab.
 */
public class BassebombeItemGroup extends CreativeModeTab {

	public BassebombeItemGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(getBassebombeCraft().getCreativeTabItem());
	}

}
