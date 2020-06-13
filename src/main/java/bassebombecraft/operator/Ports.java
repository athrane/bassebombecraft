package bassebombecraft.operator;

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
	 * Set block position.
	 * 
	 * @param pos block position.
	 */
	void setBlockPosition(BlockPos pos);

	/**
	 * Return block position.
	 * 
	 * @return block position.
	 */
	BlockPos getBlockPosition();

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
}
