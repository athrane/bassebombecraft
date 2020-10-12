package bassebombecraft.item.composite.projectile.path;

import static bassebombecraft.config.ModConfiguration.decreaseGravityProjectilePathItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.path.DeaccelerateProjectilePath;
import bassebombecraft.operator.projectile.path.DecreaseGravityProjectilePath;
import net.minecraft.entity.Entity;

/**
 * Increase gravity projectile path item.
 * 
 * The tagged projectile is processed in
 * {@linkplain GenericCompositeProjectileEntity} where the
 * {@linkplain DeaccelerateProjectilePath} operator is executed.
 */
public class DecreaseGravityProjectilePathItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = DecreaseGravityProjectilePathItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> DecreaseGravityProjectilePath.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public DecreaseGravityProjectilePathItem() {
		super(NAME, decreaseGravityProjectilePathItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
