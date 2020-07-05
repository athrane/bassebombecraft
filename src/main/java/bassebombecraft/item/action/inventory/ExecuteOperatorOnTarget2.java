package bassebombecraft.item.action.inventory;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} which executes
 * embedded operators.
 * 
 * Strategy is invoked when target isn't invoker.
 * 
 * The ports is updated with the world the invoker entity and the target
 * entity..
 */
public class ExecuteOperatorOnTarget2 implements InventoryItemActionStrategy {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators.
	 */
	Operator2[] ops;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operators.
	 * @param ops   operators executed by the strategy.
	 */
	public ExecuteOperatorOnTarget2(Ports ports, Operator2[] ops) {
		this.ops = ops;
		this.ports = ports;
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return (!targetIsInvoker);
	}

	@Override
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		ports.setLivingEntity1(invoker);
		ports.setLivingEntity2(target);
		ports.setWorld(world);
		run(ports, ops);
	}

}
