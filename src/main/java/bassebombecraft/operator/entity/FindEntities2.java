package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which finds a set of
 * {@linkplain LivingEntity} within vicinity to a position.
 */
public class FindEntities2 implements Operator2 {

	/**
	 * Function to get position for location of targets.
	 */
	Function<Ports, BlockPos> fnGetPosition;

	/**
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld;

	/**
	 * Function to set entities in ports.
	 */
	Function<Ports, BlockPos> fnSetEntities;

	/**
	 * Function to set projectiles.
	 */
	BiConsumer<Ports, Entity[]> bcSetEntities;

	/**
	 * Search range.
	 */
	int range;

	/**
	 * Constructor.
	 * 
	 * @param fnGetPosition function to get position.
	 * @param fnGetWorld    function to get world.
	 * @param bcSetEntities function to set entities.
	 * @param range         search range for entities.
	 */
	public FindEntities2(Function<Ports, BlockPos> fnGetPosition, Function<Ports, World> fnGetWorld,
			BiConsumer<Ports, Entity[]> bcSetEntities, int range) {
		this.fnGetPosition = fnGetPosition;
		this.fnGetWorld = fnGetWorld;
		this.bcSetEntities = bcSetEntities;
		this.range = range;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with block position #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 * 
	 * Instance sets found entities as entity array #1 in the ports.
	 * 
	 * @param range search range for entities.
	 */
	public FindEntities2(int range) {
		this(getFnGetBlockPosition1(), getFnWorld1(), getBcSetEntities1(), range);
	}

	@Override
	public void run(Ports ports) {
		BlockPos position = applyV(fnGetPosition, ports);
		World world = applyV(fnGetWorld, ports);

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(position).grow(range);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);
		Entity[] entityArray = entities.stream().toArray(Entity[]::new);

		// store entities
		bcSetEntities.accept(ports, entityArray);
	}

}
