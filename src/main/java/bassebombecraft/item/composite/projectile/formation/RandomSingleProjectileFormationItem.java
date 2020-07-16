package bassebombecraft.item.composite.projectile.formation;

import static bassebombecraft.config.ModConfiguration.randomSingleProjectileFormationItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.RandomSingleProjectileFormation2;

/**
 * Random single projectile formation item.
 */
public class RandomSingleProjectileFormationItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = RandomSingleProjectileFormationItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public RandomSingleProjectileFormationItem() {
		super(NAME, randomSingleProjectileFormationItem);
	}

	@Override
	public Operator2 createOperator() {
		return new RandomSingleProjectileFormation2();
	}

}
