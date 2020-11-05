package bassebombecraft.item.composite.projectile.formation.modifier;

import static bassebombecraft.config.ModConfiguration.oscillatingRotation180DProjectileFormationModifierItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.formation.modifier.OscillatingRotation180DProjectileFormationModifier;

/**
 * Oscillating rotation (180 degrees) projectile formation modifier item.
 */
public class OscillatingRotation180DProjectileFormationModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = OscillatingRotation180DProjectileFormationModifierItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public OscillatingRotation180DProjectileFormationModifierItem() {
		super(oscillatingRotation180DProjectileFormationModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return new OscillatingRotation180DProjectileFormationModifier();
	}

}
