package bassebombecraft.item.composite;

import static bassebombecraft.config.ModConfiguration.compositeMagic;

/**
 * Composite magic item.
 */
public class CompositeMagic extends GenericCompositeLogicItem {

	/**
	 * Item identifier.
	 */	
	public static final String NAME = CompositeMagic.class.getSimpleName();

	public CompositeMagic() {
		super(NAME, compositeMagic);
	}

}
