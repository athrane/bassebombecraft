package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.iceBlockBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.SpawnIceBlock2;
import bassebombecraft.operator.projectile.ShootSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;

/**
 * Book of ice block implementation.
 */
public class IceBlockBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = IceBlockBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnIceBlock2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	public IceBlockBook() {
		super(iceBlockBook, getInstance(), splOp.get());
	}
}
