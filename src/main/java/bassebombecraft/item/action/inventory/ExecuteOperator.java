package bassebombecraft.item.action.inventory;

import bassebombecraft.operator.Operators;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} which executes
 * supplied operators.
 * 
 * Action isn't invoked when target is invoker.
 */
public class ExecuteOperator implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = ExecuteOperator.class.getSimpleName();

	/**
	 * Operator execution.
	 */
	Operators operators;

	/**
	 * ExecuteOperator constructor.
	 * 
	 * @param operators operators to execute.
	 */
	public ExecuteOperator(Operators operators) {
		this.operators = operators;
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
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		operators.run(invoker, target);
	}

}
