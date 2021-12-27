package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.util.function.Predicates.fnGetNullPredicateForEntity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which finds a set of
 * {@linkplain Entity} within vicinity to a position.
 * 
 * The operator supports searching using filtering {@linkplain Predicate} to
 * eliminate found candidates.
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
	 * Function to set projectiles.
	 */
	BiConsumer<Ports, Entity[]> bcSetEntities;

	/**
	 * Function to get predicate.
	 */
	Function<Ports, Predicate<Entity>> fnGetPredicate;

	/**
	 * Function to get search range.
	 */
	Function<Ports, Integer> fnGetRange;

	/**
	 * Constructor.
	 * 
	 * @param fnGetPosition  function to get position.
	 * @param fnGetWorld     function to get world.
	 * @param fnGetPredicate function to get predicate for entity exclusion during
	 *                       entity search.
	 * @param fnGetRange     function to get search range for entities.
	 * @param bcSetEntities  function to set entities.
	 */
	public FindEntities2(Function<Ports, BlockPos> fnGetPosition, Function<Ports, World> fnGetWorld,
			Function<Ports, Predicate<Entity>> fnGetPredicate, Function<Ports, Integer> fnGetRange,
			BiConsumer<Ports, Entity[]> bcSetEntities) {
		this.fnGetPosition = fnGetPosition;
		this.fnGetWorld = fnGetWorld;
		this.fnGetPredicate = fnGetPredicate;
		this.fnGetRange = fnGetRange;
		this.bcSetEntities = bcSetEntities;
	}

	/**
	 * Constructor.
	 * 
	 * Search is configured with a null exclusion predicate which excludes nothing.
	 * 
	 * @param fnGetPosition function to get position.
	 * @param fnGetWorld    function to get world.
	 * @param fnGetRange    function to get search range for entities.
	 * @param bcSetEntities function to set entities.
	 */
	public FindEntities2(Function<Ports, BlockPos> fnGetPosition, Function<Ports, World> fnGetWorld,
			Function<Ports, Integer> fnGetRange, BiConsumer<Ports, Entity[]> bcSetEntities) {
		this(fnGetPosition, fnGetWorld, fnGetNullPredicateForEntity(), fnGetRange, bcSetEntities);
	}

	/**
	 * Constructor.
	 * 
	 * Search is configured with a null exclusion predicate which excludes nothing.
	 * 
	 * Instance is configured with block position #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 * 
	 * Instance sets found entities as entity array #1 in the ports.
	 * 
	 * @param fnGetRange function to search range for entities.
	 */
	public FindEntities2(Function<Ports, Integer> fnGetRange) {
		this(getFnGetBlockPosition1(), getFnWorld1(), fnGetRange, getBcSetEntities1());
	}

	@Override
	public void run(Ports ports) {
		BlockPos position = applyV(fnGetPosition, ports);
		World world = applyV(fnGetWorld, ports);
		Predicate<Entity> predidate = applyV(fnGetPredicate, ports);
		Integer range = fnGetRange.apply(ports);

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(position).grow(range);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb, predidate);
		Entity[] entityArray = entities.stream().toArray(Entity[]::new);

		// store entities
		bcSetEntities.accept(ports, entityArray);
	}

}
