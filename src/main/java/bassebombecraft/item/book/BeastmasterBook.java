package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.beastmasterBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.Charm2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of beast master.
 */
public class BeastmasterBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = BeastmasterBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> Charm2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public BeastmasterBook() {
		super(beastmasterBook, getInstance(), splOp.get());
	}
}
