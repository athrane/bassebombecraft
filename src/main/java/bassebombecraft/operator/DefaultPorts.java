package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultPorts implements Ports {

	/**
	 * Living entity instance.
	 */
	LivingEntity livingEntity;

	/**
	 * Block position instance.
	 */
	BlockPos blockPos;

	/**
	 * World object.
	 */
	World world;
	
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
	 */
	DefaultPorts() {
		this.result = true;
	}
	
	/**
	 * Constructor
	 * 
	 * @param entity living entity.
	 */
	DefaultPorts(LivingEntity livingEntity) {
		this();
		this.livingEntity = livingEntity;
	}

	/**
	 * Constructor
	 * 
	 * @param pos block position.
	 */
	DefaultPorts(BlockPos pos) {
		this();
		this.blockPos = pos;
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
	public void setBlockPosition(BlockPos pos) {
		this.blockPos = pos;
	}

	@Override
	public BlockPos getBlockPosition() {
		return blockPos;
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void setWorld(World world) {
		this.world = world;
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
	 * @return ports.
	 */
	public static Ports getInstance() {
		return new DefaultPorts();
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

	/**
	 * Factory method.
	 *  
	 * @param pos block position.
	 * 
	 * @return ports.
	 */
	public static Ports getInstance(BlockPos pos) {
		return new DefaultPorts(pos);
	}

}
