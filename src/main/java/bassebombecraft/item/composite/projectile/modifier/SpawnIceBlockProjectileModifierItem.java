package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.spawnIceBlockProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.raytraceresult.SpawnIceBlock2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.entity.Entity;

/**
 * Spawn ice block projectile modifier item.
 * 
 * The tagged projectile is processed in
 * {@linkplain ProjectileModifierEventHandler} where the
 * {@linkplain SpawnIceBlock2} operator is executed.
 */
public class SpawnIceBlockProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = SpawnIceBlockProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> SpawnIceBlock2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public SpawnIceBlockProjectileModifierItem() {
		super(spawnIceBlockProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
