package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.multipleArrowsBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootArrow2;
import bassebombecraft.operator.projectile.formation.TrifurcatedProjectileFormation2;

/**
 * Book of mutiple arrows implementation.
 */
public class MultipleArrowsBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = MultipleArrowsBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOps = () -> {
		Operator2 formationOp = new TrifurcatedProjectileFormation2();
		Operator2 projectileOp = new ShootArrow2();
		return new Sequence2(formationOp, projectileOp);
	};

	public MultipleArrowsBook() {
		super(ITEM_NAME, multipleArrowsBook, getInstance(), splOps.get());
	}
}
