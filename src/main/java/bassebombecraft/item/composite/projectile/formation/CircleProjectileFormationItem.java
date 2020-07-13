package bassebombecraft.item.composite.projectile.formation;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.CircleProjectileFormation2;

/**
 * Circle projectile formation item.
 */
public class CircleProjectileFormationItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = CircleProjectileFormationItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public CircleProjectileFormationItem() {
		super(NAME, ModConfiguration.circleProjectileFormationItem);
	}

	@Override
	public Operator2 createOperator() {
		return new CircleProjectileFormation2();
	}

}
