package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.emitVerticalForceBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.EmitVerticalForce2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of emit vertical force implementation.
 */
public class EmitVerticalForceBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = EmitVerticalForceBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(),
				p -> EmitVerticalForce2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public EmitVerticalForceBook() {
		super(emitVerticalForceBook, getInstance(), splOp.get());
	}
}
