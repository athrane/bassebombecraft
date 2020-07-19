package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.teleportMobProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.TeleportMob2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.entity.Entity;

/**
 * Teleport mob projectile modifier item.
 */
public class TeleportMobProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = TeleportMobProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> TeleportMob2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public TeleportMobProjectileModifierItem() {
		super(NAME, teleportMobProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
