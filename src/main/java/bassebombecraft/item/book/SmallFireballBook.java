package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;

/**
 * Book of small fireball implementation.
 */
public class SmallFireballBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SmallFireballBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	public SmallFireballBook() {
		super(smallFireballBook, getInstance(), splOps.get());
	}
}
