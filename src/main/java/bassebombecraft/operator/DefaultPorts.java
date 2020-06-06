package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;

public class DefaultPorts implements Ports {

	/**
	 * Living entity instance.
	 */
	LivingEntity livingEntity;

	/**
	 * {@linkplain LivingEntity} supplier.
	 */
	Supplier<LivingEntity> splLivingEntity = () -> livingEntity;

	/**
	 * Result of operator execution.
	 */
	boolean result;

	/**
	 * Constructor
	 * 
	 * @param entity living entity.
	 */
	DefaultPorts(LivingEntity livingEntity) {
		this.result = true;
		this.livingEntity = livingEntity;
	}

	@Override
	public Supplier<LivingEntity> getSplLivingEntity() {
		return splLivingEntity;
	}

	
	@Override
	public LivingEntity getLivingEntity() {
		return livingEntity;
	}

	
	@Override
	public void setResultAsSucces() {
		this.result = true;		
	}

	@Override
	public void setResultAsFailed() {
		this.result = false;				
	}

	@Override
	public boolean getResult() {
		return result;
	}

	/**
	 * Factory method.
	 *  
	 * @param entity living entity.
	 * 
	 * @return ports.
	 */
	public static Ports getInstance(LivingEntity livingEntity) {
		return new DefaultPorts(livingEntity);
	}
}
