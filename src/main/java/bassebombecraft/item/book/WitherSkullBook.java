package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.witherSkullBook;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootWitherSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;

/**
 * Book of wither skull implementation.
 */
public class WitherSkullBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = WitherSkullBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootWitherSkullProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	public WitherSkullBook() {
		super(witherSkullBook, getInstance(), splOps.get());
	}
}
