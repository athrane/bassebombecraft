package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.projectileFormationRandomModifierItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.modifier.ProjectileFormationRandomModifier;

/**
 * Projectile formation random modifieritem.
 */
public class ProjectileFormationRandomModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = ProjectileFormationRandomModifierItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public ProjectileFormationRandomModifierItem() {
		super(NAME, projectileFormationRandomModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return new ProjectileFormationRandomModifier();
	}

}
