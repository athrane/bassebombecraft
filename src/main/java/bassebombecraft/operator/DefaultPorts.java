package bassebombecraft.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
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
	 * String #1.
	 */
	String string1;

	/**
	 * String #2.
	 */
	String string2;
	
	/**
	 * World object.
	 */
	World world;

	/**
	 * Result of operator execution.
	 */
	boolean result;

	/**
	 * integer counter. Initially set to 0.
	 */
	int counter;

	/**
	 * Axis aligned bounding box.
	 */
	AxisAlignedBB aabb;

	/**
	 * Matrix stack.
	 */
	MatrixStack matrixStack;

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
	 * String #1 getter.
	 */
	static Function<Ports, String> fnGetString1 = p -> p.getString1();

	/**
	 * String #1 setter.
	 */
	static BiConsumer<Ports, String> bcSetString1 = (Ports p, String s) -> p.setString1(s);

	/**
	 * String #2 getter.
	 */
	static Function<Ports, String> fnGetString2 = p -> p.getString2();

	/**
	 * String #2 setter.
	 */
	static BiConsumer<Ports, String> bcSetString2 = (Ports p, String s) -> p.setString2(s);
	
	/**
	 * Constructor
	 */
	DefaultPorts() {
		this.result = true;
		this.counter = 0;
	}

	@Override
	public LivingEntity getLivingEntity() {
		return livingEntity;
	}

	@Override
	public Ports setLivingEntity(LivingEntity entity) {
		this.livingEntity = entity;
		return this;
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
	public String getString1() {
		return this.string1;
	}

	@Override
	public Ports setString1(String value) {
		this.string1 = value;
		return this;
	}

	@Override
	public String getString2() {
		return this.string2;
	}

	@Override
	public Ports setString2(String value) {
		this.string2 = value;
		return this;
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public Ports setWorld(World world) {
		this.world = world;
		return this;
	}

	@Override
	public Ports setResultAsSucces() {
		this.result = true;
		return this;
	}

	@Override
	public Ports setResultAsFailed() {
		this.result = false;
		return this;
	}

	@Override
	public boolean getResult() {
		return result;
	}

	@Override
	public Ports setCounter(int value) {
		this.counter = value;
		return this;
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

	@Override
	public AxisAlignedBB getAabb() {
		return aabb;
	}

	@Override
	public Ports setAabb(AxisAlignedBB aabb) {
		this.aabb = aabb;
		return this;
	}

	@Override
	public MatrixStack getMatrixStack() {
		return matrixStack;
	}

	@Override
	public Ports setMatrixStack(MatrixStack ms) {
		this.matrixStack = ms;
		return this;
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

	public static Function<Ports, String> getFnGetString1() {
		return fnGetString1;
	}

	public static BiConsumer<Ports, String> getBcSetString1() {
		return bcSetString1;
	}

	public static Function<Ports, String> getFnGetString2() {
		return fnGetString2;
	}

	public static BiConsumer<Ports, String> getBcSetString2() {
		return bcSetString2;
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
		return new DefaultPorts().setLivingEntity(livingEntity);
	}

	/**
	 * Factory method.
	 * 
	 * @param pos block position.
	 * 
	 * @return ports.
	 */
	public static Ports getInstance(BlockPos pos) {
		DefaultPorts ports = new DefaultPorts();
		ports.setBlockPosition1(pos);
		;
		return ports;
	}

}
