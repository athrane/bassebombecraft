package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.teleportProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.entity.Teleport2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;

/**
 * Teleport projectile item.
 */
public class TeleportProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = TeleportProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public TeleportProjectileItem() {
		super(NAME, teleportProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		Operator2 projectileLogicOp = new Teleport2();
		return new ShootOperatorEggProjectile2(projectileLogicOp);
	}

}
