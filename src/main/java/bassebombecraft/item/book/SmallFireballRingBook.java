package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballRingBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootFireball2;
import bassebombecraft.operator.projectile.formation.ProjectileRingFormation2;

/**
 * Book of small fireball ring implementation.
 */
public class SmallFireballRingBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = SmallFireballRingBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOps = () -> {
		Operator2 formationOp = new ProjectileRingFormation2();
		Operator2 projectileOp = new ShootFireball2();
		return new Sequence2(formationOp, projectileOp);
	};

	public SmallFireballRingBook() {
		super(ITEM_NAME, smallFireballRingBook, getInstance(), splOps.get());
	}
}
