package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.charmProjectileModifierItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.modifier.RandomPathProjectileModifier;

/**
 * Random path projectile modifier item.
 */
public class RandomPathProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = RandomPathProjectileModifierItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public RandomPathProjectileModifierItem() {
		super(NAME, charmProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return new RandomPathProjectileModifier();
	}

}
