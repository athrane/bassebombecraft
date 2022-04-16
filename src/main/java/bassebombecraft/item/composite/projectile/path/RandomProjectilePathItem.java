package bassebombecraft.item.composite.projectile.path;

import static bassebombecraft.config.ModConfiguration.randomProjectilePathItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.path.RandomProjectilePath;
import net.minecraft.world.entity.Entity;

/**
 * Random projectile path item.
 * 
 * The tagged projectile is processed in {@linkplain GenericCompositeProjectileEntity}
 * where the {@linkplain RandomProjectilePath} operator is executed.
 */
public class RandomProjectilePathItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = RandomProjectilePathItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> RandomProjectilePath.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public RandomProjectilePathItem() {
		super(randomProjectilePathItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
