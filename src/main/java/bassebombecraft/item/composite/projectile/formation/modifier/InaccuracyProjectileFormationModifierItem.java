package bassebombecraft.item.composite.projectile.formation.modifier;

import static bassebombecraft.config.ModConfiguration.inaccuracyProjectileFormationModifierItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.modifier.InaccuracyProjectileFormationModifier;

/**
 * Inaccuracy formation modifier item.
 */
public class InaccuracyProjectileFormationModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = InaccuracyProjectileFormationModifierItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public InaccuracyProjectileFormationModifierItem() {
		super(NAME, inaccuracyProjectileFormationModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return new InaccuracyProjectileFormationModifier();
	}

}
