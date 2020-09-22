package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.teleportBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.TeleportInvoker2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of teleport implementation.
 */
public class TeleportBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = TeleportBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> TeleportInvoker2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public TeleportBook() {
		super(ITEM_NAME, teleportBook, getInstance(), splOp.get());
	}
}
