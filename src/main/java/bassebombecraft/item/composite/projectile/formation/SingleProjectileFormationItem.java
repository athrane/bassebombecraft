package bassebombecraft.item.composite.projectile.formation;

import static bassebombecraft.config.ModConfiguration.singleProjectileFormationItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;

/**
 * Single projectile formation item.
 */
public class SingleProjectileFormationItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = SingleProjectileFormationItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public SingleProjectileFormationItem() {
		super(NAME, singleProjectileFormationItem);
	}

	@Override
	public Operator2 createOperator() {
		return new SingleProjectileFormation2();
	}

}
