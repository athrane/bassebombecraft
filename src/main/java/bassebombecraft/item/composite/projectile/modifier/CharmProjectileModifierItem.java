package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.charmProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.Charm2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.entity.Entity;

/**
 * Charm mob projectile modifier item.
 */
public class CharmProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = CharmProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> Charm2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public CharmProjectileModifierItem() {
		super(NAME, charmProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
