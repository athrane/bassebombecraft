package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.cobwebBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.SpawnCobweb2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of cobweb implementation.
 */
public class CobwebBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = CobwebBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnCobweb2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public CobwebBook() {
		super(ITEM_NAME, cobwebBook, getInstance(), splOp.get());
	}
}
