package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;
import java.util.function.Function;

import static bassebombecraft.BassebombeCraft.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
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
	 * Operator identifier.
	 */
	public static final String NAME = Electrocute2.class.getSimpleName();

	/**
	 * Function to get source entity.
	 */
	Function<Ports, LivingEntity> fnGetSource;

	/**
	 * Operator for spawning graphical effect for.
	 */
	static Operator2 addEffectOp = new AddGraphicalEffectAtClient2();

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
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity.
	 */
	public Electrocute2(Function<Ports, LivingEntity> fnGetSource) {
		this.fnGetSource = fnGetSource;
		aoeRange = 5;
		addEffectPorts = getInstance();
		// minExplosionRadius = explodeMinExplosionRadius.get();
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as source from ports.
	 */
	public Electrocute2() {
		this(getFnGetLivingEntity1());
	}

	@Override
	public Ports run(Ports ports) {
		getBassebombeCraft().getLogger().debug("Electrocute2 run..");
		
		// get source
		LivingEntity source = fnGetSource.apply(ports);
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

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(source, target);
			if (!isInvoker)
				applyEffect(source, target);
		}

		return ports;
	}

	void applyEffect(LivingEntity source, LivingEntity target) {
		// apply damage to target
		
		// send graphical effect to client
		addEffectPorts.setLivingEntity1(source);
		addEffectPorts.setLivingEntity2(target);
		Operators2.run(addEffectPorts, addEffectOp);
		
		getBassebombeCraft().getLogger().debug("Electrocute2 source="+source);
		getBassebombeCraft().getLogger().debug("Electrocute2 target="+target);		
	}

}
