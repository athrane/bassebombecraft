package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.teleportBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.TeleportInvoker2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;

/**
 * Book of teleport implementation.
 */
public class TeleportBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = TeleportBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 projectileLogicOp = new TeleportInvoker2();
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootOperatorEggProjectile2(projectileLogicOp);
		return new Sequence2(formationOp, projectileOp);
	};

	public TeleportBook() {
		super(ITEM_NAME, teleportBook, getInstance(), splOp.get());
	}
}
