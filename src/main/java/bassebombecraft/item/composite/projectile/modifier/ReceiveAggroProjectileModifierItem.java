package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.operator.projectile.modifier.tag.ReceiveAggro2;
import net.minecraft.world.entity.Entity;

/**
 * Receive mob aggro projectile modifier item.
 * 
 * The tagged projectile is processed in
 * {@linkplain ProjectileModifierEventHandler} where the
 * {@linkplain ReceiveAggro2} operator is executed.
 */
public class ReceiveAggroProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = ReceiveAggroProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> ReceiveAggro2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public ReceiveAggroProjectileModifierItem() {
		super(receiveAggroProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
