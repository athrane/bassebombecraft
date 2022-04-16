package bassebombecraft.item.composite.projectile.path;

import static bassebombecraft.config.ModConfiguration.accelerateProjectilePathItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.path.DeaccelerateProjectilePath;
import net.minecraft.world.entity.Entity;

/**
 * Deaccelerate projectile path item.
 * 
 * The tagged projectile is processed in
 * {@linkplain GenericCompositeProjectileEntity} where the
 * {@linkplain DeaccelerateProjectilePath} operator is executed.
 */
public class DeaccelerateProjectilePathItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = DeaccelerateProjectilePathItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> DeaccelerateProjectilePath.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public DeaccelerateProjectilePathItem() {
		super(accelerateProjectilePathItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
