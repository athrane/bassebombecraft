package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.meteorProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.ShootMeteor2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.world.entity.Entity;

/**
 * Meteor projectile modifier item.
 * 
 * The tagged projectile is processed in
 * {@linkplain ProjectileModifierEventHandler} where the
 * {@linkplain ShootMeteor2} operator is executed.
 */
public class MeteorProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = MeteorProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> ShootMeteor2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public MeteorProjectileModifierItem() {
		super(meteorProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
