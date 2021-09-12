package bassebombecraft.item.composite.projectile.modifier;

import static bassebombecraft.config.ModConfiguration.spawnFlamingChickenProjectileModifierItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.raytraceresult.SpawnFlamingChicken2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.entity.Entity;

/**
 * Spawn failed phoenix projectile modifier item.
 * 
 * The tagged projectile is processed in
 * {@linkplain ProjectileModifierEventHandler} where the
 * {@linkplain SpawnFlamingChicken2} operator is executed.
 */
public class SpawnFlamingChickenProjectileModifierItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = SpawnFlamingChickenProjectileModifierItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, Entity[]> fnGetProjectiles = getFnGetEntities1();
		Function<Ports, String> fnGetTag = p -> SpawnFlamingChicken2.NAME;
		return new TagProjectileWithProjectileModifier(fnGetProjectiles, fnGetTag);
	};

	/**
	 * Constructor.
	 */
	public SpawnFlamingChickenProjectileModifierItem() {
		super(spawnFlamingChickenProjectileModifierItem);
	}

	@Override
	public Operator2 createOperator() {
		return splOp.get();
	}

}
