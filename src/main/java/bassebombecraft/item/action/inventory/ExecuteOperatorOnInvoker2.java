package bassebombecraft.item.action.inventory;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} which executes
 * embedded operators.
 * 
 * Strategy is invoked when target is invoker.
 * 
 * The ports is updated with the world and the invoker entity as living entity #1.
 * 
 * The ports is reused across invocations.
 */
public class ExecuteOperatorOnInvoker2 implements InventoryItemActionStrategy {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operator.
	 */
	Operator2 op;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operators.
	 * @param op    operator executed by the strategy.
	 */
	public ExecuteOperatorOnInvoker2(Ports ports, Operator2 op) {
		this.op = op;
		this.ports = ports;
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return (targetIsInvoker);
	}

	@Override
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		ports.setLivingEntity1(invoker);
		ports.setWorld(world);
		run(ports, op);
	}

}
