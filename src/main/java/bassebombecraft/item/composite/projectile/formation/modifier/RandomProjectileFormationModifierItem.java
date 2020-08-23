package bassebombecraft.item.composite.projectile.formation.modifier;

import static bassebombecraft.config.ModConfiguration.randomProjectileFormationModifierItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.modifier.RandomProjectileFormationModifier;

/**
 * Random projectile formation modifier item.
 */
public class RandomProjectileFormationModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = RandomProjectileFormationModifierItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public RandomProjectileFormationModifierItem() {
		super(NAME, randomProjectileFormationModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return new RandomProjectileFormationModifier();
	}

}
