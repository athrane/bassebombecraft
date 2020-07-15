package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.largeFireballBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootLargeFireballProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;

/**
 * Book of large fireball implementation.
 */
public class LargeFireballBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = LargeFireballBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootLargeFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	public LargeFireballBook() {
		super(ITEM_NAME, largeFireballBook, getInstance(), splOp.get());
	}
}
