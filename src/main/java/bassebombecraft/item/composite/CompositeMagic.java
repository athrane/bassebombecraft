package bassebombecraft.item.composite;

import static bassebombecraft.config.ModConfiguration.compositeMagic;

/**
 * Composite magic.
 */
public class CompositeMagic extends GenericCompositeLogicItem {

	public static final String ITEM_NAME = CompositeMagic.class.getSimpleName();

	public CompositeMagic() {
		super(ITEM_NAME, compositeMagic);
	}

}
