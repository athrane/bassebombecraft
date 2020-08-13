package bassebombecraft.item.composite.projectile.path;

import static bassebombecraft.config.ModConfiguration.sineProjectilePathItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.path.SineProjectilePath;
import bassebombecraft.projectile.GenericProjectileEntity;
import net.minecraft.entity.Entity;

/**
 * Sine projectile path item.
 * 
 * The tagged projectile is processed in {@linkplain GenericProjectileEntity}
 * where the {@linkplain SineProjectilePath} operator is executed.
 */
public class SineProjectilePathItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = SineProjectilePathItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> SineProjectilePath.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public SineProjectilePathItem() {
		super(NAME, sineProjectilePathItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
