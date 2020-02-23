package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnRavagerBook;

import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.entity.SpawnRavager;

/**
 * Book of friendly ravager implementation.
 */
public class SpawnRavagerBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnRavagerBook.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		SpawnRavager spawnOp = new SpawnRavager(ops.getSplLivingEntity());
		ops.setOperator(spawnOp);
		return ops;
	};

	public SpawnRavagerBook() {
		super(ITEM_NAME, spawnRavagerBook, new ShootOperatorEggProjectile(splOp.get()));
	}
}
