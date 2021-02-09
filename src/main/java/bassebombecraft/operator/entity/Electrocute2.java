package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.electrocuteAoeRange;
import static bassebombecraft.config.ModConfiguration.electrocuteDamage;
import static bassebombecraft.config.ModConfiguration.electrocuteDuration;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which applies AOE
 * effect electrocute to nearby mobs.
 * 
 * Damage is assigned to mobs and an graphical effect is sent to clients.
 */
public class Electrocute2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Electrocute2.class.getSimpleName();

	/**
	 * Damage getter.
	 * 
	 * Reads the damage value form the configuration and not from the ports, even
	 * though ports is supplied as a parameter.
	 * 
	 * The damage can't be read from ports because there isn't any 'free' double
	 * left, double #1 is used by the {@linkplain AddGraphicalEffectAtClient2}.
	 */
	static Function<Ports, Double> fnGetDamage = p -> electrocuteDamage.get();

	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Function to get true source entity (e.g projectile thrower).
	 */
	Function<Ports, Entity> fnGetTrueSource;

	/**
	 * 
	 * Operator for adding damage and spawning graphical effect.
	 */
	static Operator2 effectOp = new Sequence2(new AddDamage2(getFnGetEntity1(), getFnGetEntity2(), fnGetDamage),
			new AddGraphicalEffectAtClient2(NAME));

	/**
	 * Effect ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports effectPorts;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource     function to get source entity.
	 * @param fnGetTrueSource function to get thrower entity.
	 */
	public Electrocute2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTrueSource) {
		this.fnGetSource = fnGetSource;
		this.fnGetTrueSource = fnGetTrueSource;
		effectPorts = getInstance();
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as (e.g projectile thrower) from ports.
	 */
	public Electrocute2() {
		this(getFnGetEntity1(), getFnGetEntity2());
	}

	@Override
	public Ports run(Ports ports) {

		// get source
		Entity source = fnGetSource.apply(ports);
		if (source == null)
			return ports;

		// get position of projectile entity
		BlockPos position = source.getPosition();

		// get original source, i.e. invoker
		Entity originalSource = fnGetTrueSource.apply(ports);
		if (originalSource == null)
			return ports;

		// get world
		World world = source.getEntityWorld();

		// get entities within AABB
		int aoeRange = electrocuteAoeRange.get();
		AxisAlignedBB aabb = new AxisAlignedBB(position).grow(aoeRange);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		for (LivingEntity target : entities) {

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(originalSource, target);
			if (!isInvoker)
				applyEffect(source, target);
		}

		return ports;
	}

	/**
	 * Apply effect.
	 * 
	 * @param source source entity.
	 * @param target target entity.
	 */
	void applyEffect(Entity source, LivingEntity target) {
		effectPorts.setEntity1(source);
		effectPorts.setEntity2(target);
		effectPorts.setDouble1(electrocuteDuration.get().doubleValue());
		Operators2.run(effectPorts, effectOp);
	}

}
