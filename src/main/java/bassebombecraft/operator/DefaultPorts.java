package bassebombecraft.operator;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import bassebombecraft.color.Color4f;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class DefaultPorts implements Ports {

	/**
	 * Defines if debug features should be enabled.
	 */
	boolean debugEnabled;
	
	/**
	 * Living entity #1.
	 */
	LivingEntity livingEntity1;

	/**
	 * Living entity #2.
	 */
	LivingEntity livingEntity2;

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
	 * Living entity array #1.
	 */
	LivingEntity[] livingEntities1;
	
	/**
	 * Block position #1.
	 */
	BlockPos blockPos1;

	/**
	 * Block position #2.
	 */
	BlockPos blockPos2;

	/**
	 * Item stack #1
	 */
	ItemStack itemStack1; 
	
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
	 * Integer #1.
	 */
	int integer1;

	/**
	 * Vector2f #1.
	 */
	Vec2 vector2f1;
	
	/**
	 * Vector3d #1.
	 */
	Vec3 vector3d1;

	/**
	 * Vector3d array #1.
	 */
	Vec3[] vectors3d1;

	/**
	 * Vector3d array #2.
	 */
	Vec3[] vectors3d2;

	/**
	 * Color4f #1.
	 */
	Color4f color4f1;

	/**
	 * Color4f #2.
	 */
	Color4f color4f2;
	
	/**
	 * Effect instance #1.
	 */
	MobEffectInstance effectInstance1;

	/**
	 * World object.
	 */
	Level world;

	/**
	 * Ray trace result #1.
	 */
	HitResult rayTraceResult1;

	/**
	 * Result of operator execution.
	 */
	boolean result;

	/**
	 * Integer counter. Initially set to 0.
	 */
	int counter;

	/**
	 * Axis aligned bounding box.
	 */
	AABB aabb;

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
	 * Living entity array #1 setter.
	 */
	static BiConsumer<Ports, LivingEntity[]> bcSetLivingEntities1 = (p, e) -> p.setLivingEntities1(e);

	/**
	 * Living Entity array #1 getter.
	 */
	static Function<Ports, LivingEntity[]> fnGetLivingEntities1 = p -> p.getLivingEntities1();
	
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
	 * Item stack #1 getter.
	 */
	static Function<Ports, ItemStack> fnGetItemStack1 = p -> p.getItemStack1();

	/**
	 * Item stack #1 setter.
	 */
	static BiConsumer<Ports, ItemStack> bcSetItemStack1 = (p, is) -> p.setItemStack1(is);
	
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
	 * Integer #1 getter.
	 */
	static Function<Ports, Integer> fnGetInteger1 = p -> p.getInteger1();

	/**
	 * Integer #1 setter.
	 */
	static BiConsumer<Ports, Integer> bcSetInteger1 = (p, d) -> p.setInteger1(d);

	/**
	 * Vector2f #1 getter.
	 */
	static Function<Ports, Vec2> fnGetVector2f1 = p -> p.getVector2f1();

	/**
	 * Vector2f #1 setter.
	 */
	static BiConsumer<Ports, Vec2> bcSetVector2f1 = (p, v) -> p.setVector2f1(v);
	
	/**
	 * Vector3d #1 getter.
	 */
	static Function<Ports, Vec3> fnGetVector3d1 = p -> p.getVector1();

	/**
	 * Vector3d #1 setter.
	 */
	static BiConsumer<Ports, Vec3> bcSetVector3d1 = (p, v) -> p.setVector1(v);

	/**
	 * Vector3d array #1 setter.
	 */
	static BiConsumer<Ports, Vec3[]> bcSetVectors3d1 = (p, v) -> p.setVectors1(v);

	/**
	 * Vector3d array #1 getter.
	 */
	static Function<Ports, Vec3[]> fnGetVectors3d1 = p -> p.getVectors1();

	/**
	 * Vector3d array #2 setter.
	 */
	static BiConsumer<Ports, Vec3[]> bcSetVectors3d2 = (p, v) -> p.setVectors2(v);

	/**
	 * Vector3d array #2 getter.
	 */
	static Function<Ports, Vec3[]> fnGetVectors3d2 = p -> p.getVectors2();

	/**
	 * Color4f #1 getter.
	 */
	static Function<Ports, Color4f> fnGetColor4f1 = p -> p.getColor4f1();

	/**
	 * Color4f #1 setter.
	 */
	static BiConsumer<Ports, Color4f> bcSetColor4f1 = (p, v) -> p.setColor4f1(v);

	/**
	 * Color4f #2 getter.
	 */
	static Function<Ports, Color4f> fnGetColor4f2 = p -> p.getColor4f2();

	/**
	 * Color4f #2 setter.
	 */
	static BiConsumer<Ports, Color4f> bcSetColor4f2 = (p, v) -> p.setColor4f2(v);

	/**
	 * Ray trace result #1 getter.
	 */
	static Function<Ports, HitResult> fnGetRayTraceResult1 = p -> p.getRayTraceResult1();

	/**
	 * Ray trace result #1 setter.
	 */
	static BiConsumer<Ports, HitResult> bcSetRayTraceResult1 = (p, r) -> p.setRayTraceResult1(r);

	/**
	 * Effect instance #1 getter.
	 */
	static Function<Ports, MobEffectInstance> fnGetEffecInstance1 = p -> p.getEffectInstance1();

	/**
	 * Effect instance #1 setter.
	 */
	static BiConsumer<Ports, MobEffectInstance> bcSetEffectInstance1 = (p, i) -> p.setEffectInstance1(i);

	/**
	 * World #1 getter.
	 */
	static Function<Ports, Level> fnGetWorld1 = p -> p.getWorld();

	/**
	 * World #1 setter.
	 */
	static BiConsumer<Ports, Level> bcSetWorld1 = (p, w) -> p.setWorld(w);

	/**
	 * AABB #1 getter.
	 */
	static Function<Ports, AABB> fnGetAabb1 = p -> p.getAabb1();

	/**
	 * AABB #1 setter.
	 */
	static BiConsumer<Ports, AABB> bcSetAabb1 = (p, aabb) -> p.setAabb1(aabb);

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
	protected DefaultPorts() {
		this.debugEnabled = false;
		this.result = true;
		this.counter = 0;
	}
	
	@Override
	public Ports enableDebug() {
		this.debugEnabled = true;
		return this;		
	}

	@Override
	public boolean isDebugEnabled() {
		return this.debugEnabled;
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
	public LivingEntity[] getLivingEntities1() {
		return livingEntities1;
	}

	@Override
	public Ports setLivingEntities1(LivingEntity[] entities) {
		this.livingEntities1 = entities;
		return this;
	}
	
	@Override
	public BlockPos getBlockPosition1() {
		return blockPos1;
	}

	@Override
	public Ports setBlockPosition1(BlockPos pos) {
		this.blockPos1 = pos;
		return this;
	}
	
	@Override
	public BlockPos getBlockPosition2() {
		return blockPos2;
	}
	
	@Override
	public Ports setBlockPosition2(BlockPos pos) {
		this.blockPos2 = pos;
		return this;
	}
	
	@Override
	public ItemStack getItemStack1() {
		return itemStack1;
	}
	
	@Override
	public Ports setItemStack1(ItemStack itemStack) {
		this.itemStack1 = itemStack;
		return this;
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
	public int getInteger1() {
		return integer1;
	}

	@Override
	public Ports setInteger1(int value) {
		this.integer1 = value;
		return this;
	}

	
	@Override
	public Vec2 getVector2f1() {
		return vector2f1;
	}

	@Override
	public Ports setVector2f1(Vec2 vec) {
		this.vector2f1 = vec;
		return this;
	}

	@Override
	public Vec3 getVector1() {
		return vector3d1;
	}

	@Override
	public Ports setVector1(Vec3 vec) {
		this.vector3d1 = vec;
		return this;
	}

	@Override
	public Vec3[] getVectors1() {
		return vectors3d1;
	}

	@Override
	public Ports setVectors1(Vec3[] vec) {
		this.vectors3d1 = vec;
		return this;
	}

	@Override
	public Vec3[] getVectors2() {
		return vectors3d2;
	}

	@Override
	public Ports setVectors2(Vec3[] vec) {
		this.vectors3d2 = vec;
		return this;
	}

	@Override
	public Color4f getColor4f1() {
		return color4f1;
	}

	@Override
	public Ports setColor4f1(Color4f color) {
		this.color4f1 = color;
		return this;
	}

	@Override
	public Color4f getColor4f2() {
		return color4f2;
	}

	@Override
	public Ports setColor4f2(Color4f color) {
		this.color4f2 = color;
		return this;
	}

	@Override
	public HitResult getRayTraceResult1() {
		return rayTraceResult1;
	}

	@Override
	public Ports setRayTraceResult1(HitResult rtr) {
		this.rayTraceResult1 = rtr;
		return this;
	}

	@Override
	public MobEffectInstance getEffectInstance1() {
		return effectInstance1;
	}

	@Override
	public Ports setEffectInstance1(MobEffectInstance instance) {
		this.effectInstance1 = instance;
		return this;
	}

	@Override
	public Level getWorld() {
		return world;
	}

	@Override
	public Ports setWorld(Level world) {
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
	public AABB getAabb1() {
		return aabb;
	}

	@Override
	public Ports setAabb1(AABB aabb) {
		this.aabb = aabb;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefaultPorts [");		
		builder.append("Result=" + getResult());
		builder.append(", Counter=" + getCounter());
		builder.append(", Integer1=" + getInteger1());
		if (getString1() != null)
			builder.append(", String1=" + getString1());
		if (getString2() != null)
			builder.append(", String2=" + getString2());
		if (getDouble1() != null)
			builder.append(", Double1=" + getDouble1());
		if (getAabb1() != null)
			builder.append(", Aabb1=" + getAabb1());
		if (getBlockPosition1() != null)
			builder.append(", BlockPosition1=" + getBlockPosition1());
		if (getBlockPosition2() != null)
			builder.append(", BlockPosition2=" + getBlockPosition2());
		if (getColor4f1() != null)
			builder.append(", Color4f1=" + getColor4f1());
		if (getColor4f2() != null)
			builder.append(", Color4f2=" + getColor4f2());
		if (getDamageSource1() != null)
			builder.append(", DamageSource1=" + getDamageSource1());
		if (getEffectInstance1() != null)
			builder.append(", EffectInstance1=" + getEffectInstance1());
		if (getEntity1() != null)
			builder.append(", Entity1=" + getEntity1());
		if (getEntity2() != null)
			builder.append(", Entity2=" + getEntity2());
		if (getEntities1() != null)
			builder.append(", Entities1=" + Arrays.toString(getEntities1()));
		if (getLivingEntity1() != null)
			builder.append(", LivingEntity1=" + getLivingEntity1());
		if (getLivingEntity2() != null)
			builder.append(", LivingEntity2=" + getLivingEntity2());
		if (getEntities1() != null)
			builder.append(", LivingEntities1=" + Arrays.toString(getLivingEntities1()));
		if (getVector2f1() != null)
			builder.append(", Vector2f1=" + getVector2f1());		
		if (getVector1() != null)
			builder.append(", Vector1=" + getVector1());
		if (getVectors1() != null)
			builder.append(", Vectors1=" + Arrays.toString(getVectors1()));
		if (getVectors2() != null)
			builder.append(", Vectors2=" + Arrays.toString(getVectors2()));
		builder.append("]");				
		return builder.toString();
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

	public static Function<Ports, LivingEntity[]> getFnGetLivingEntities1() {
		return fnGetLivingEntities1;
	}

	public static BiConsumer<Ports, LivingEntity[]> getBcSetLivingEntities1() {
		return bcSetLivingEntities1;
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

	public static Function<Ports, ItemStack> getFnGetItemStack1() {
		return fnGetItemStack1;
	}

	public static BiConsumer<Ports, ItemStack> getBcSetItemStack1() {
		return bcSetItemStack1;
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

	public static Function<Ports, Integer> getFnGetInteger1() {
		return fnGetInteger1;
	}

	public static BiConsumer<Ports, Integer> getBcSetInteger1() {
		return bcSetInteger1;
	}

	public static Function<Ports, Vec2> getFnGetVector2f1() {
		return fnGetVector2f1;
	}

	public static BiConsumer<Ports, Vec2> getBcSetVector2f1() {
		return bcSetVector2f1;
	}
	
	public static Function<Ports, Vec3> getFnGetVector1() {
		return fnGetVector3d1;
	}

	public static BiConsumer<Ports, Vec3> getBcSetVector1() {
		return bcSetVector3d1;
	}

	public static Function<Ports, Vec3[]> getFnGetVectors1() {
		return fnGetVectors3d1;
	}

	public static BiConsumer<Ports, Vec3[]> getBcSetVectors1() {
		return bcSetVectors3d1;
	}

	public static Function<Ports, Vec3[]> getFnGetVectors2() {
		return fnGetVectors3d2;
	}

	public static BiConsumer<Ports, Vec3[]> getBcSetVectors2() {
		return bcSetVectors3d2;
	}

	public static Function<Ports, Color4f> getFnGetColor4f1() {
		return fnGetColor4f1;
	}

	public static BiConsumer<Ports, Color4f> getBcSetColor4f1() {
		return bcSetColor4f1;
	}

	public static Function<Ports, Color4f> getFnGetColor4f2() {
		return fnGetColor4f2;
	}

	public static BiConsumer<Ports, Color4f> getBcSetColor4f2() {
		return bcSetColor4f2;
	}

	public static Function<Ports, HitResult> getFnGetRayTraceResult1() {
		return fnGetRayTraceResult1;
	}

	public static BiConsumer<Ports, HitResult> getBcSetRayTraceResult1() {
		return bcSetRayTraceResult1;
	}

	public static Function<Ports, MobEffectInstance> getFnEffectInstance1() {
		return fnGetEffecInstance1;
	}

	public static BiConsumer<Ports, MobEffectInstance> getBcSetEffectInstance1() {
		return bcSetEffectInstance1;
	}

	public static Function<Ports, Level> getFnWorld1() {
		return fnGetWorld1;
	}

	public static BiConsumer<Ports, Level> getBcSetWorld1() {
		return bcSetWorld1;
	}

	public static Function<Ports, AABB> getFnAabb1() {
		return fnGetAabb1;
	}

	public static BiConsumer<Ports, AABB> getBcSetAabb1() {
		return bcSetAabb1;
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
	@Deprecated
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
	@Deprecated
	public static Ports getInstance(BlockPos pos) {
		DefaultPorts ports = new DefaultPorts();
		ports.setBlockPosition1(pos);
		return ports;
	}

}
