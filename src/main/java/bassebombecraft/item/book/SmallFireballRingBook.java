package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballRingBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;
import bassebombecraft.operator.projectile.formation.CircleProjectileFormation2;

/**
 * Book of small fireball ring implementation.
 */
public class SmallFireballRingBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SmallFireballRingBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new CircleProjectileFormation2();
		Operator2 projectileOp = new ShootFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	public SmallFireballRingBook() {
		super(smallFireballRingBook, getInstance(), splOp.get());
	}
}
