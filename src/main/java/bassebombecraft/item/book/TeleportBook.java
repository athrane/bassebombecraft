package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.teleportBook;

import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.entity.Teleport;

/**
 * Book of teleport implementation.
 */
public class TeleportBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = TeleportBook.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		Teleport teleportOp = new Teleport(ops.getSplLivingEntity(), ops.getSplRayTraceResult());
		ops.setOperator(teleportOp);
		return ops;
	};

	public TeleportBook() {
		super(ITEM_NAME, teleportBook, new ShootOperatorEggProjectile(splOp.get()));
	}
}
