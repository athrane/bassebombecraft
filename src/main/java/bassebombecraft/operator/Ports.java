package bassebombecraft.operator;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Ports which provides input and output from operators.
 */
public interface Ports {

	/**
	 * Set result of operator execution as a success.
	 * 
	 * @return ports.
	 */
	Ports setResultAsSucces();

	/**
	 * Set result of operator execution as a failed.
	 * 
	 * @return ports.
	 */
	Ports setResultAsFailed();

	/**
	 * Get result of operation execution.
	 */
	boolean getResult();

	/**
	 * Get {@linkplain LivingEntity} #1.
	 * 
	 * @return living entity #1.
	 */
	LivingEntity getLivingEntity1();

	/**
	 * Set {@linkplain LivingEntity} #1.
	 * 
	 * @return ports.
	 * 
	 */
	Ports setLivingEntity1(LivingEntity entity);

	/**
	 * Get {@linkplain LivingEntity} #2.
	 * 
	 * @return living entity #2.
	 */
	LivingEntity getLivingEntity2();

	/**
	 * Set {@linkplain LivingEntity} #2.
	 * 
	 * @return ports.
	 * 
	 */
	Ports setLivingEntity2(LivingEntity entity);
	
	/**
	 * Set {@linkplain BlockPos} #1.
	 * 
	 * @param pos block position.
	 */
	void setBlockPosition1(BlockPos pos);

	/**
	 * Return {@linkplain BlockPos} #1.
	 * 
	 * @return block position.
	 */
	BlockPos getBlockPosition1();

	/**
	 * Set {@linkplain BlockPos} #2.
	 * 
	 * @param pos block position.
	 */
	void setBlockPosition2(BlockPos pos);

	/**
	 * Return {@linkplain BlockPos} #2.
	 * 
	 * @return block position.
	 */
	BlockPos getBlockPosition2();

	/**
	 * Get string #1.
	 * 
	 * @return string #1 .
	 */
	String getString1();

	/**
	 * Set string  #1.
	 * 
	 * @return ports.
	 */
	Ports setString1(String value);

	/**
	 * Get string #2.
	 * 
	 * @return string #2 .
	 */
	String getString2();

	/**
	 * Set string #2.
	 * 
	 * @return ports.
	 */
	Ports setString2(String value);
	
	/**
	 * Return world.
	 * 
	 * @return world.
	 */
	World getWorld();

	/**
	 * Set world.
	 * 
	 * @param world world object.
	 */
	Ports setWorld(World world);

	/**
	 * Set counter value.
	 * 
	 * @param value value to set a counter.
	 * 
	 * @return ports.
	 */
	Ports setCounter(int value);

	/**
	 * Return counter value.
	 * 
	 * @return counter value.
	 */
	int getCounter();

	/**
	 * Increment value of counter with 1.
	 * 
	 * @return counter value.
	 */
	int incrementCounter();

	/**
	 * Get AABB.
	 * 
	 * @return AABB.
	 */
	AxisAlignedBB getAabb();

	/**
	 * Set AABB.
	 * 
	 * @param aabb AABB to set.
	 *
	 * @return ports.
	 */
	Ports setAabb(AxisAlignedBB aabb);

	/**
	 * Get matrix stack.
	 * 
	 * @return matrix stack.
	 */
	MatrixStack getMatrixStack();

	/**
	 * Set matrix stack .
	 * 
	 * @param ms matrix stack.
	 * 
	 * @return ports.
	 */
	Ports setMatrixStack(MatrixStack ms);

}
