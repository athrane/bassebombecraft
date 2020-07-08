package bassebombecraft.operator.item.action;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	 * Operators for the job.
	 */
	Operator2[] ops;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operators.
	 * @param ops   operators executed by the action.
	 */
	ExecuteOperatorAsAction2(Ports ports, Operator2[] ops) {
		this.ops = ops;
		this.ports = ports;
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		ports.setWorld(world);
		ports.setLivingEntity1(entity);
		run(ports, ops);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

	/**
	 * Factory method.
	 * 
	 * @param ports ports used by operators.
	 * @param ops   operators executed by the action.
	 * 
	 * @return action.
	 */
	public static RightClickedItemAction getInstance(Ports ports, Operator2[] ops) {
		return new ExecuteOperatorAsAction2(ports, ops);
	}

}
