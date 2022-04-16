package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.explodeMobWhenKilledProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.Explode2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.world.entity.Entity;

/**
 * Explode mob when killed projectile modifier item.
 * 
 * When a mob is killed by a projectile with this modifier then the mob explode
 * upon death.
 * 
 * The tagged projectile is processed in
 * {@linkplain ProjectileModifierEventHandler} where the {@linkplain Explode2}
 * operator is executed.
 */
public class ExplodeMobWhenKilledProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = ExplodeMobWhenKilledProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> Explode2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public ExplodeMobWhenKilledProjectileModifierItem() {
		super(explodeMobWhenKilledProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
