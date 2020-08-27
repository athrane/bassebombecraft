package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.digMobHoleBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.DigMobHole2;
import bassebombecraft.operator.projectile.ShootLlamaProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of mob hole.
 */
public class DigMobHoleBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = DigMobHoleBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootLlamaProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> DigMobHole2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public DigMobHoleBook() {
		super(ITEM_NAME, digMobHoleBook, getInstance(), splOp.get());
	}
}
