package bassebombecraft.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
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
	 * String stream #1.
	 */
	Stream<String> strings1;

	/**
	 * Double #1.
	 */
	Double double1;

	/**
	 * Vector #1.
	 */
	Vec3d vector1;

	/**
	 * Vector array #1.
	 */
	Vec3d[] vectors1;

	/**
	 * Entity #1.
	 */
	Entity entitiy1;

	/**
	 * Entity #2.
	 */
	Entity entitiy2;

	/**
	 * Entity array #1.
	 */
	Entity[] entities1;

	/**
	 * Effect instance #1.
	 */
	EffectInstance effectInstance1;

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
	 * Damage source.
	 */
	DamageSource damageSource;
	
	/**
	 * Living entity #1 getter.
	 */
	static Function<Ports, LivingEntity> fnGetLivingEntity1 = p -> p.getLivingEntity1();

	/**
	 * Living entity #1 setter.
	 */
	static BiConsumer<Ports, LivingEntity> bcSetLivingEntity1 = (p, le) -> p.setLivingEntity1(le);

	/**
	 * Living entity #2 getter.
	 */
	static Function<Ports, LivingEntity> fnGetLivingEntity2 = p -> p.getLivingEntity2();

	/**
	 * Living entity #2 setter.
	 */
	static BiConsumer<Ports, LivingEntity> bcSetLivingEntity2 = (p, le) -> p.setLivingEntity2(le);

	/**
	 * Block position #1 getter.
	 */
	static Function<Ports, BlockPos> fnGetBlockPos1 = p -> p.getBlockPosition1();

	/**
	 * Block position #1 setter.
	 */
	static BiConsumer<Ports, BlockPos> bcSetBlockPos1 = (p, bp) -> p.setBlockPosition1(bp);

	/**
	 * Block position #2 getter.
	 */
	static Function<Ports, BlockPos> fnGetBlockPos2 = p -> p.getBlockPosition2();

	/**
	 * Block position #2 setter.
	 */
	static BiConsumer<Ports, BlockPos> bcSetBlockPos2 = (p, bp) -> p.setBlockPosition2(bp);

	/**
	 * String #1 getter.
	 */
	static Function<Ports, String> fnGetString1 = p -> p.getString1();

	/**
	 * String #1 setter.
	 */
	static BiConsumer<Ports, String> bcSetString1 = (p, s) -> p.setString1(s);

	/**
	 * String #2 getter.
	 */
	static Function<Ports, String> fnGetString2 = p -> p.getString2();

	/**
	 * String #2 setter.
	 */
	static BiConsumer<Ports, String> bcSetString2 = (p, s) -> p.setString2(s);

	/**
	 * String stream #1 setter.
	 */
	static BiConsumer<Ports, Stream<String>> bcSetStrings1 = (p, s) -> p.setStrings1(s);

	/**
	 * String stream #1 getter.
	 */
	static Function<Ports, Stream<String>> fnGetStrings1 = p -> p.getStrings1();

	/**
	 * Double #1 getter.
	 */
	static Function<Ports, Double> fnGetDouble1 = p -> p.getDouble1();

	/**
	 * Double #1 setter.
	 */
	static BiConsumer<Ports, Double> bcSetDouble1 = (p, d) -> p.setDouble1(d);

	/**
	 * Vector #1 getter.
	 */
	static Function<Ports, Vec3d> fnGetVector1 = p -> p.getVector1();

	/**
	 * Vector #1 setter.
	 */
	static BiConsumer<Ports, Vec3d> bcSetVector1 = (p, v) -> p.setVector1(v);

	/**
	 * Vector array #1 setter.
	 */
	static BiConsumer<Ports, Vec3d[]> bcSetVectors1 = (p, v) -> p.setVectors1(v);

	/**
	 * Vector array #1 getter.
	 */
	static Function<Ports, Vec3d[]> fnGetVectors1 = p -> p.getVectors1();

	/**
	 * Entity #1 setter.
	 */
	static BiConsumer<Ports, Entity> bcSetEntity1 = (p, e) -> p.setEntity1(e);

	/**
	 * Entity #1 getter.
	 */
	static Function<Ports, Entity> fnGetEntity1 = p -> p.getEntity1();

	/**
	 * Entity #2 setter.
	 */
	static BiConsumer<Ports, Entity> bcSetEntity2 = (p, e) -> p.setEntity2(e);

	/**
	 * Entity #2 getter.
	 */
	static Function<Ports, Entity> fnGetEntity2 = p -> p.getEntity2();

	/**
	 * Entity array #1 setter.
	 */
	static BiConsumer<Ports, Entity[]> bcSetEntities1 = (p, e) -> p.setEntities1(e);

	/**
	 * Entity array #1 getter.
	 */
	static Function<Ports, Entity[]> fnGetEntities1 = p -> p.getEntities1();

	/**
	 * Ray trace result #1 getter.
	 */
	static Function<Ports, RayTraceResult> fnGetRayTraceResult1 = p -> p.getRayTraceResult1();

	/**
	 * Ray trace result #1 setter.
	 */
	static BiConsumer<Ports, RayTraceResult> bcSetRayTraceResult1 = (p, r) -> p.setRayTraceResult1(r);

	/**
	 * Effect instance #1 getter.
	 */
	static Function<Ports, EffectInstance> fnGetEffecInstance1 = p -> p.getEffectInstance1();

	/**
	 * Effect instance #1 setter.
	 */
	static BiConsumer<Ports, EffectInstance> bcSetEffectInstance1 = (p, i) -> p.setEffectInstance1(i);

	/**
	 * World #1 getter.
	 */
	static Function<Ports, World> fnGetWorld1 = p -> p.getWorld();

	/**
	 * World #1 setter.
	 */
	static BiConsumer<Ports, World> bcSetWorld1 = (p, w) -> p.setWorld(w);

	/**
	 * AABB #1 getter.
	 */
	static Function<Ports, AxisAlignedBB> fnGetAabb1 = p -> p.getAabb1();

	/**
	 * AABB #1 setter.
	 */
	static BiConsumer<Ports, AxisAlignedBB> bcSetAabb1 = (p, aabb) -> p.setAabb1(aabb);

	/**
	 * Matrix stack #1 getter.
	 */
	static Function<Ports, MatrixStack> fnGetMatrixStack1 = p -> p.getMatrixStack1();

	/**
	 * Matrix stack #1 setter.
	 */
	static BiConsumer<Ports, MatrixStack> bcSetMatrixStack1 = (p, ms) -> p.setMatrixStack1(ms);

	/**
	 * Damage source #1 getter.
	 */
	static Function<Ports, DamageSource> fnGetDamageSource1 = p -> p.getDamageSource1();

	/**
	 * Damage source #1 setter.
	 */
	static BiConsumer<Ports, DamageSource> bcSetDamageSource1 = (p, ds) -> p.setDamageSource1(ds);
	
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
	public Ports setBlockPosition1(BlockPos pos) {
		this.blockPos1 = pos;
		return this;
	}

	@Override
	public BlockPos getBlockPosition1() {
		return blockPos1;
	}

	@Override
	public Ports setBlockPosition2(BlockPos pos) {
		this.blockPos2 = pos;
		return this;
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
		return string2;
	}

	@Override
	public Ports setString2(String value) {
		this.string2 = value;
		return this;
	}

	@Override
	public Stream<String> getStrings1() {
		return strings1;
	}

	@Override
	public Ports setStrings1(Stream<String> values) {
		this.strings1 = values;
		return this;
	}

	@Override
	public Double getDouble1() {
		return double1;
	}

	@Override
	public Ports setDouble1(Double value) {
		this.double1 = value;
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
	public Entity getEntity1() {
		return entitiy1;
	}

	@Override
	public Ports setEntity1(Entity entity) {
		this.entitiy1 = entity;
		return this;
	}

	@Override
	public Entity getEntity2() {
		return entitiy2;
	}

	@Override
	public Ports setEntity2(Entity entity) {
		this.entitiy2 = entity;
		return this;
	}

	@Override
	public Entity[] getEntities1() {
		return entities1;
	}

	@Override
	public Ports setEntities1(Entity[] entities) {
		this.entities1 = entities;
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
	public EffectInstance getEffectInstance1() {
		return effectInstance1;
	}

	@Override
	public Ports setEffectInstance1(EffectInstance instance) {
		this.effectInstance1 = instance;
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
	public AxisAlignedBB getAabb1() {
		return aabb;
	}

	@Override
	public Ports setAabb1(AxisAlignedBB aabb) {
		this.aabb = aabb;
		return this;
	}

	@Override
	public MatrixStack getMatrixStack1() {
		return matrixStack;
	}

	@Override
	public Ports setMatrixStack1(MatrixStack ms) {
		this.matrixStack = ms;
		return this;
	}
	
	@Override
	public DamageSource getDamageSource1() {
		return damageSource;
	}

	@Override
	public Ports setDamageSource1(DamageSource ds) {
		this.damageSource = ds;
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

	public static Function<Ports, Stream<String>> getFnGetStrings1() {
		return fnGetStrings1;
	}

	public static BiConsumer<Ports, Stream<String>> getBcSetStrings1() {
		return bcSetStrings1;
	}

	public static Function<Ports, Double> getFnGetDouble1() {
		return fnGetDouble1;
	}

	public static BiConsumer<Ports, Double> getBcSetDouble1() {
		return bcSetDouble1;
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

	public static Function<Ports, Entity> getFnGetEntity1() {
		return fnGetEntity1;
	}

	public static BiConsumer<Ports, Entity> getBcSetEntity1() {
		return bcSetEntity1;
	}

	public static Function<Ports, Entity> getFnGetEntity2() {
		return fnGetEntity2;
	}

	public static BiConsumer<Ports, Entity> getBcSetEntity2() {
		return bcSetEntity2;
	}

	public static Function<Ports, Entity[]> getFnGetEntities1() {
		return fnGetEntities1;
	}

	public static BiConsumer<Ports, Entity[]> getBcSetEntities1() {
		return bcSetEntities1;
	}

	public static Function<Ports, RayTraceResult> getFnGetRayTraceResult1() {
		return fnGetRayTraceResult1;
	}

	public static BiConsumer<Ports, RayTraceResult> getBcSetRayTraceResult1() {
		return bcSetRayTraceResult1;
	}

	public static Function<Ports, EffectInstance> getFnEffectInstance1() {
		return fnGetEffecInstance1;
	}

	public static BiConsumer<Ports, EffectInstance> getBcSetEffectInstance1() {
		return bcSetEffectInstance1;
	}

	public static Function<Ports, World> getFnWorld1() {
		return fnGetWorld1;
	}

	public static BiConsumer<Ports, World> getBcSetWorld1() {
		return bcSetWorld1;
	}

	public static Function<Ports, AxisAlignedBB> getFnAabb1() {
		return fnGetAabb1;
	}

	public static BiConsumer<Ports, AxisAlignedBB> getBcSetAabb1() {
		return bcSetAabb1;
	}

	public static Function<Ports, MatrixStack> getFnMaxtrixStack1() {
		return fnGetMatrixStack1;
	}

	public static BiConsumer<Ports, MatrixStack> getBcSetMaxtrixStack1() {
		return bcSetMatrixStack1;
	}

	public static Function<Ports, DamageSource> getFnDamageSource1() {
		return fnGetDamageSource1;
	}

	public static BiConsumer<Ports, DamageSource> getBcSetDamageSource1() {
		return bcSetDamageSource1;
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
