package bassebombecraft.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Ports which provides input and output from operators.
 */
public interface Ports {

	/**
	 * Get {@linkplain LivingEntity} supplier.
	 * 
	 * @return entity supplier.
	 */
	Supplier<LivingEntity> getSplLivingEntity();

	/**
	 * Get {@linkplain LivingEntity}.
	 * 
	 * @return living entity.
	 */
	LivingEntity getLivingEntity();

	/**
	 * Set result of operator execution as a success.
	 */
	void setResultAsSucces();

	/**
	 * Set result of operator execution as a failed.
	 */
	void setResultAsFailed();

	/**
	 * Get result of operation execution.
	 */
	boolean getResult();

	/**
	 * Set block position #1.
	 * 
	 * @param pos block position.
	 */
	void setBlockPosition1(BlockPos pos);

	/**
	 * Return block position #1.
	 * 
	 * @return block position.
	 */
	BlockPos getBlockPosition1();

	/**
	 * Get functional interface {@linkplain Function} for block position #1 getter.
	 * 
	 * @return functional interface for block position #1 getter.
	 */
	Function<Ports, BlockPos> getFnGetBlockPosition1();

	/**
	 * Get functional interface {@linkplain BiConsumer} for block position #1
	 * setter.
	 * 
	 * @return functional interface for block position #1 setter.
	 */
	BiConsumer<Ports, BlockPos> getBcSetBlockPosition1();

	/**
	 * Set block position #2.
	 * 
	 * @param pos block position.
	 */
	void setBlockPosition2(BlockPos pos);

	/**
	 * Return block position #2.
	 * 
	 * @return block position.
	 */
	BlockPos getBlockPosition2();

	/**
	 * Get functional interface {@linkplain Function} for block position #2 getter.
	 * 
	 * @return functional interface for block position #2 getter.
	 */
	Function<Ports, BlockPos> getFnGetBlockPosition2();

	/**
	 * Get functional interface {@linkplain BiConsumer} for block position #2
	 * setter.
	 * 
	 * @return functional interface for block position #2 setter.
	 */
	BiConsumer<Ports, BlockPos> getBcSetBlockPosition2();

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
	void setWorld(World world);

	/**
	 * Set counter value.
	 * 
	 * @param value value to set a counter.
	 */
	void setCounter(int value);

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
}
