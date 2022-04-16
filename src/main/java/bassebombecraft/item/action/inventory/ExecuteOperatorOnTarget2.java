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
 * Strategy is invoked when target isn't invoker.
 * 
 * The ports is updated with the world, the invoker as living entity #1 and the
 * target as living entity #2.
 */
public class ExecuteOperatorOnTarget2 implements InventoryItemActionStrategy {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operator.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param ports    ports used by operators.
	 * @param operator operator executed by the strategy.
	 */
	public ExecuteOperatorOnTarget2(Ports ports, Operator2 operator) {
		this.ports = ports;
		this.operator = operator;
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
		ports.setLivingEntity1(invoker);
		ports.setLivingEntity2(target);
		ports.setWorld(world);
		run(ports, operator);
	}

}
