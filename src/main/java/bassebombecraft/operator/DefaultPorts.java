package bassebombecraft.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;
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
	 * Block position instance #1.
	 */
	BlockPos blockPos1;

	/**
	 * Block position instance #2.
	 */
	BlockPos blockPos2;

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
	 * integer counter. Initially set to 0.
	 */
	int counter;

	/**
	 * Block position #1 getter.
	 */
	static Function<Ports, BlockPos> fnGetBlockPos1 = p -> p.getBlockPosition1();

	/**
	 * Block position #1 setter.
	 */	
	static BiConsumer<Ports, BlockPos> bcSetBlockPos1 = (Ports p, BlockPos bp) -> p.setBlockPosition1(bp);
	
	/**
	 * Block position #2 getter.
	 */
	static Function<Ports, BlockPos> fnGetBlockPos2 = p -> p.getBlockPosition2();

	/**
	 * Block position #2 setter.
	 */	
	static BiConsumer<Ports, BlockPos> bcSetBlockPos2 = (Ports p, BlockPos bp) -> p.setBlockPosition2(bp);
				
	/**
	 * Constructor
	 */
	DefaultPorts() {
		this.result = true;
		this.counter = 0;
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
		this.blockPos1 = pos;
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
	public void setBlockPosition1(BlockPos pos) {
		this.blockPos1 = pos;
	}

	@Override
	public BlockPos getBlockPosition1() {
		return blockPos1;
	}


	@Override
	public void setBlockPosition2(BlockPos pos) {
		this.blockPos2 = pos;
	}
	
	@Override
	public BlockPos getBlockPosition2() {
		return blockPos2;
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

	@Override
	public void setCounter(int value) {
		this.counter = value;
	}

	@Override
	public int getCounter() {
		return counter;
	}

	@Override
	public int incrementCounter() {
		counter++;
		return counter;
	}

	public static Function<Ports, BlockPos> getFnGetBlockPosition1() {
		return fnGetBlockPos1;
	}
	
	public static BiConsumer<Ports, BlockPos> getBcSetBlockPosition1() {
		return bcSetBlockPos1;
	}

	public static Function<Ports, BlockPos> getFnGetBlockPosition2() {
		return fnGetBlockPos2;
	}
	
	public static BiConsumer<Ports, BlockPos> getBcSetBlockPosition2() {
		return bcSetBlockPos2;
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
