package bassebombecraft.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DefaultPorts implements Ports {

	/**
	 * Living entity #1.
	 */
	LivingEntity livingEntity1;

	/**
	 * Living entity #2.
	 */
	LivingEntity livingEntity2;
	
	/**
	 * Block position #1.
	 */
	BlockPos blockPos1;

	/**
	 * Block position #2.
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
	 * Vector #1.
	 */
	Vec3d vector1;

	/**
	 * Vector array #1.
	 */
	Vec3d[] vectors1;
	
	/**
	 * Operator #1.
	 */
	Operator2 op1;
	
	/**
	 * World object.
	 */
	World world;

	/**
	 * Ray trace result #1.
	 */
	RayTraceResult rayTraceResult1;
	
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
	 * Living entity #1 getter.
	 */
	static Function<Ports, LivingEntity> fnGetLivingEntity1 = p -> p.getLivingEntity1();

	/**
	 * Living entity #1 setter.
	 */
	static BiConsumer<Ports, LivingEntity> bcSetLivingEntity1 = (Ports p, LivingEntity le) -> p.setLivingEntity1(le);

	/**
	 * Living entity #2 getter.
	 */
	static Function<Ports, LivingEntity> fnGetLivingEntity2 = p -> p.getLivingEntity2();

	/**
	 * Living entity #2 setter.
	 */
	static BiConsumer<Ports, LivingEntity> bcSetLivingEntity2 = (Ports p, LivingEntity le) -> p.setLivingEntity2(le);
	
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
	 * Vector #1 getter.
	 */
	static Function<Ports, Vec3d> fnGetVector1 = p -> p.getVector1();

	/**
	 * Vector #1 setter.
	 */
	static BiConsumer<Ports, Vec3d> bcSetVector1 = (Ports p, Vec3d v) -> p.setVector1(v);
	
	/**
	 * Vector array #1 setter.
	 */
	static BiConsumer<Ports, Vec3d[]> bcSetVectors1 = (Ports p, Vec3d[] v) -> p.setVectors1(v);

	/**
	 * Vector array #1 getter.
	 */
	static Function<Ports, Vec3d[]> fnGetVectors1 = p -> p.getVectors1();
	
	/**
	 * Operator #1 getter.
	 */
	static Function<Ports, Operator2> fnGetOperator1 = p -> p.getOperator1();

	/**
	 * Operator #1 setter.
	 */
	static BiConsumer<Ports, Operator2> bcSetOperator1 = (Ports p, Operator2 op) -> p.setOperator1(op);

	/**
	 * Ray trace result #1 getter.
	 */
	static Function<Ports, RayTraceResult> fnGetRayTraceResult1 = p -> p.getRayTraceResult1();

	/**
	 * Ray trace result #1 setter.
	 */
	static BiConsumer<Ports, RayTraceResult> bcSetRayTraceResult1 = (Ports p, RayTraceResult r) -> p.setRayTraceResult1(r);
	
	/**
	 * Constructor
	 */
	DefaultPorts() {
		this.result = true;
		this.counter = 0;
	}

	@Override
	public LivingEntity getLivingEntity1() {
		return livingEntity1;
	}

	@Override
	public Ports setLivingEntity1(LivingEntity entity) {
		this.livingEntity1 = entity;
		return this;
	}

	
	@Override
	public LivingEntity getLivingEntity2() {
		return livingEntity2;
	}

	@Override
	public Ports setLivingEntity2(LivingEntity entity) {
		this.livingEntity2 = entity;
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
		return string1;
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
	public Vec3d getVector1() {
		return vector1;
	}
	
	@Override
	public Ports setVector1(Vec3d vec) {
		this.vector1 = vec;
		return this;
	}

	@Override
	public Vec3d[] getVectors1() {
		return vectors1;
	}
	
	@Override
	public Ports setVectors1(Vec3d[] vec) {
		this.vectors1 = vec;
		return this;
	}
	
	@Override
	public Operator2 getOperator1() {
		return op1;
	}

	@Override
	public Ports setOperator1(Operator2 op) {
		this.op1 = op;
		return this;
	}

	
	@Override
	public RayTraceResult getRayTraceResult1() {
		return rayTraceResult1;
	}

	@Override
	public Ports setRayTraceResult1(RayTraceResult rtr) {
		this.rayTraceResult1 = rtr;
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

	public static Function<Ports, LivingEntity> getFnGetLivingEntity1() {
		return fnGetLivingEntity1;
	}

	public static BiConsumer<Ports, LivingEntity> getBcSetLivingEntity1() {
		return bcSetLivingEntity1;
	}

	public static Function<Ports, LivingEntity> getFnGetLivingEntity2() {
		return fnGetLivingEntity2;
	}

	public static BiConsumer<Ports, LivingEntity> getBcSetLivingEntity2() {
		return bcSetLivingEntity2;
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

	public static Function<Ports, Vec3d> getFnGetVector1() {
		return fnGetVector1;
	}

	public static BiConsumer<Ports, Vec3d> getBcSetVector1() {
		return bcSetVector1;
	}

	public static Function<Ports, Vec3d[]> getFnGetVectors1() {
		return fnGetVectors1;
	}

	public static BiConsumer<Ports, Vec3d[]> getBcSetVectors1() {
		return bcSetVectors1;
	}
	
	public static Function<Ports, Operator2> getFnGetOperator1() {
		return fnGetOperator1;
	}

	public static BiConsumer<Ports, Operator2> getBcSetOperator1() {
		return bcSetOperator1;
	}

	public static Function<Ports, RayTraceResult> getFnGetRayTraceResult1() {
		return fnGetRayTraceResult1;
	}

	public static BiConsumer<Ports, RayTraceResult> getBcSetRayTraceResult1() {
		return bcSetRayTraceResult1;
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
		return new DefaultPorts().setLivingEntity1(livingEntity);
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
