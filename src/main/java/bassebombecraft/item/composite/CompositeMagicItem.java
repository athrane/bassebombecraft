package bassebombecraft.item.composite;

import static bassebombecraft.config.ModConfiguration.compositeMagicItem;

/**
 * Composite magic item.
 */
public class CompositeMagicItem extends GenericCompositeLogicItem {

	/**
	 * Item identifier.
	 */	
	public static final String NAME = CompositeMagicItem.class.getSimpleName();

	public CompositeMagicItem() {
		super(NAME, compositeMagicItem);
	}

}
