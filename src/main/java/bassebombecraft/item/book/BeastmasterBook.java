package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.beastmasterBook;

import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.entity.Charm;

/**
 * Book of beast master.
 */
public class BeastmasterBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = BeastmasterBook.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		Charm charmOp = new Charm(ops.getSplLivingEntity(), ops.getSplRayTraceResult());
		ops.setOperator(charmOp);
		return ops;
	};

	public BeastmasterBook() {
		super(ITEM_NAME, beastmasterBook, new ShootOperatorEggProjectile(splOp.get()));
	}
}
