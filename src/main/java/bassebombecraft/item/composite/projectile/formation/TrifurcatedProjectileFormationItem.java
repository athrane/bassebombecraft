package bassebombecraft.item.composite.projectile.formation;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.TrifurcatedProjectileFormation2;

/**
 * Trifurcated projectile formation item.
 */
public class TrifurcatedProjectileFormationItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = TrifurcatedProjectileFormationItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public TrifurcatedProjectileFormationItem() {
		super(NAME, ModConfiguration.trifurcatedProjectileFormationItem);
	}

	@Override
	public Operator2 createOperator() {
		return new TrifurcatedProjectileFormation2();
	}

}
