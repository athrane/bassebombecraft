package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.receiveAggroBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.modifier.tag.ReceiveAggro2;

/**
 * Book of receive mob aggro implementation.
 */
public class ReceiveAggroBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = ReceiveAggroBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> ReceiveAggro2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public ReceiveAggroBook() {
		super(ITEM_NAME, receiveAggroBook, getInstance(), splOp.get());
	}

}
