package bassebombecraft.item.composite;

import bassebombecraft.operator.Operator2;
import net.minecraft.item.ItemStack;

/**
 * Resolver which can resolve composite item sequences into
 * {@linkplain ItemStack} for rendering and {@linkplain Operator2} for logic.
 */
public interface CompositeItemsResolver {

	/**
	 * Return an array of {@linkplain ItemStack} for rendering.
	 * 
	 * @return an array of {@linkplain ItemStack} for rendering.
	 */
	ItemStack[] getItemStacks();
	
	/**
	 * Return an {@linkplain Operator2} implementing logic for composite items.
	 * 
	 * @return an {@linkplain Operator2} implementing logic for composite items.
	 */
	Operator2 getOperator();
}
