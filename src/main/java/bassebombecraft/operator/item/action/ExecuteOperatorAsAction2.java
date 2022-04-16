package bassebombecraft.operator.item.action;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} interface which
 * executes operators as action.
 * 
 * The used ports is reused (and thus updated) for all invocations of the
 * operators.
 *
 */
public class ExecuteOperatorAsAction2 implements RightClickedItemAction {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators executed as action.
	 */
	Operator2 op;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operators.
	 * @param op    operator executed by the action.
	 */
	ExecuteOperatorAsAction2(Ports ports, Operator2 op) {
		this.op = op;
		this.ports = ports;
	}

	@Override
	public void onRightClick(Level world, LivingEntity entity) {
		ports.setWorld(world);
		ports.setLivingEntity1(entity);
		run(ports, op);
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

	/**
	 * Factory method.
	 * 
	 * @param ports ports used by operators.
	 * @param op    operator executed by the action.
	 * 
	 * @return action.
	 */
	public static RightClickedItemAction getInstance(Ports ports, Operator2 op) {
		return new ExecuteOperatorAsAction2(ports, op);
	}

}
