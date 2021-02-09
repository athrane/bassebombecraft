package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.electrocuteAoeRange;
import static bassebombecraft.config.ModConfiguration.electrocuteDuration;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which applies AOE
 * effect electrocute to near by mobs.
 */
public class Electrocute2 implements Operator2 {

	/**
	 * Effect duration
	 */
	static final int DURATION = 20;

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Electrocute2.class.getSimpleName();

	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Operator for spawning graphical effect for.
	 */
	static Operator2 addEffectOp = new AddGraphicalEffectAtClient2(NAME);

	/**
	 * Effect rendering ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports addEffectPorts;

	/**
	 * AOE range .
	 */
	double aoeRange;

	/**
	 * Effect duration.
	 */
	double duration;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity.
	 */
	public Electrocute2(Function<Ports, Entity> fnGetSource) {
		this.fnGetSource = fnGetSource;
		aoeRange = electrocuteAoeRange.get();
		addEffectPorts = getInstance();
		duration = electrocuteDuration.get();
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 */
	public Electrocute2() {
		this(getFnGetEntity1());
	}

	@Override
	public Ports run(Ports ports) {

		// get source
		Entity source = fnGetSource.apply(ports);
		if (source == null)
			return ports;

		// get position of dead entity
		BlockPos position = source.getPosition();

		// get world
		World world = source.getEntityWorld();

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(position).grow(aoeRange);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		for (LivingEntity target : entities) {

			// TODO: Determine why invoke is added to effect

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(source, target);
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
		// apply damage to target
		// TODO: implement

		// send graphical effect to client
		addEffectPorts.setEntity1(source);
		addEffectPorts.setEntity2(target);
		addEffectPorts.setDouble1(duration);
		Operators2.run(addEffectPorts, addEffectOp);
	}

}
