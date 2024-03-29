package bassebombecraft.item.action.inventory;

import bassebombecraft.operator.Operators;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} which executes
 * embedded operators.
 * 
 * Strategy is invoked when target isn't invoker.
 */
@Deprecated
public class ExecuteOperatorOnTarget implements InventoryItemActionStrategy {

	/**
	 * Operator execution.
	 */
	Operators operators;

	/**
	 * Constructor.
	 * 
	 * @param operators operators to execute.
	 */
	public ExecuteOperatorOnTarget(Operators operators) {
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
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		operators.run(invoker, target);
	}

}
