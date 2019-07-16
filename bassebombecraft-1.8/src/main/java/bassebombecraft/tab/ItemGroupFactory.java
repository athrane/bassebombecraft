package bassebombecraft.tab;

import net.minecraft.item.ItemGroup;

/**
 * Factory for creation of creative tabs.
 */
public class ItemGroupFactory {

	/**
	 * Create {@linkplain ItemGroup} which implements creative tab .
	 * 
	 * @param name tab name.
	 * 
	 * @return item group.
	 */
	public static ItemGroup createItemGroup(String name) {
		return new BassebombeItemGroup(name);
	}

}
