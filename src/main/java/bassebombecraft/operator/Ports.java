package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;

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
}
