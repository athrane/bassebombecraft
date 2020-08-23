package bassebombecraft.item.composite.projectile.formation;

import static bassebombecraft.config.ModConfiguration.frontAndBackProjectileFormationItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.FrontAndBackProjectileFormation2;

/**
 * Front and back projectile formation item.
 */
public class FrontAndBackProjectileFormationItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = FrontAndBackProjectileFormationItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public FrontAndBackProjectileFormationItem() {
		super(NAME, frontAndBackProjectileFormationItem);
	}

	@Override
	public Operator2 createOperator() {
		return new FrontAndBackProjectileFormation2();
	}

}
