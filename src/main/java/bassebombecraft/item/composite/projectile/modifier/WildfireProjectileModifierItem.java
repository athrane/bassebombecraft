package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.wildfireProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.Wildfire2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.entity.Entity;

/**
 * Electrocute projectile modifier item.
 * 
 * The tagged projectile is processed in
 * {@linkplain GenericCompositeProjectileEntity} where the
 * {@linkplain Wildfire2} operator is executed.
 */
public class WildfireProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = WildfireProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> Wildfire2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public WildfireProjectileModifierItem() {
		super(wildfireProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
