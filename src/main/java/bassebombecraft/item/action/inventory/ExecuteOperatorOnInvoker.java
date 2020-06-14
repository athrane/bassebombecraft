package bassebombecraft.item.action.inventory;

import bassebombecraft.operator.Operators;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} which executes
 * embedded operators.
 * 
 * Strategy is invoked when target is invoker.
 */
public class ExecuteOperatorOnInvoker implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = ExecuteOperatorOnInvoker.class.getSimpleName();

	/**
	 * Operator execution.
	 */
	Operators operators;

	/**
	 * Constructor.
	 * 
	 * @param operators operators to execute.
	 */
	public ExecuteOperatorOnInvoker(Operators operators) {
		this.operators = operators;
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
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		operators.run(invoker, target);
	}

}
