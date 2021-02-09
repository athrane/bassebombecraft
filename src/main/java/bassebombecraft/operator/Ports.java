package bassebombecraft.operator;

import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Collection of ports which provides input and output from operators.
 */
public interface Ports {

	/**
	 * Set result of operator execution as a success.
	 * 
	 * @return ports.
	 */
	Ports setResultAsSucces();

	/**
	 * Set result of operator execution as a failed.
	 * 
	 * @return ports.
	 */
	Ports setResultAsFailed();

	/**
	 * Get result of operation execution.
	 */
	boolean getResult();

	/**
	 * Get {@linkplain LivingEntity} #1.
	 * 
	 * @return living entity #1.
	 */
	LivingEntity getLivingEntity1();

	/**
	 * Set {@linkplain LivingEntity} #1.
	 * 
	 * @return ports.
	 * 
	 */
	Ports setLivingEntity1(LivingEntity entity);

	/**
	 * Get {@linkplain LivingEntity} #2.
	 * 
	 * @return living entity #2.
	 */
	LivingEntity getLivingEntity2();

	/**
	 * Set {@linkplain LivingEntity} #2.
	 * 
	 * @return ports.
	 * 
	 */
	Ports setLivingEntity2(LivingEntity entity);

	/**
	 * Return {@linkplain BlockPos} #1.
	 * 
	 * @return block position #1.
	 */
	BlockPos getBlockPosition1();

	/**
	 * Set {@linkplain BlockPos} #1.
	 * 
	 * @param pos block position #1.
	 */
	Ports setBlockPosition1(BlockPos pos);

	/**
	 * Return {@linkplain BlockPos} #2.
	 * 
	 * @return block position #2.
	 */
	BlockPos getBlockPosition2();

	/**
	 * Set {@linkplain BlockPos} #2.
	 * 
	 * @param pos block position #2.
	 */
	Ports setBlockPosition2(BlockPos pos);

	/**
	 * Get string #1.
	 * 
	 * @return string #1 .
	 */
	String getString1();

	/**
	 * Set string #1.
	 * 
	 * @return ports.
	 */
	Ports setString1(String value);

	/**
	 * Get string #2.
	 * 
	 * @return string #2 .
	 */
	String getString2();

	/**
	 * Set string #2.
	 * 
	 * @return ports.
	 */
	Ports setString2(String value);

	/**
	 * Return {@linkplain Stream<String>} #1.
	 * 
	 * @return strings #1.
	 */
	Stream<String> getStrings1();

	/**
	 * Set {@linkplain Stream<String>} #1.
	 * 
	 * @param strings string stream #1.
	 */
	Ports setStrings1(Stream<String> values);
	
	/**
	 * Get double #1.
	 * 
	 * @return double #1 .
	 */
	Double getDouble1();

	/**
	 * Set double #1.
	 * 
	 * @return ports.
	 */
	Ports setDouble1(Double value);
	
	/**
	 * Return {@linkplain Vec3d} #1.
	 * 
	 * @return vector #1.
	 */
	Vec3d getVector1();

	/**
	 * Set {@linkplain Vec3d} #1.
	 * 
	 * @param vec vector #1.
	 */
	Ports setVector1(Vec3d vec);

	/**
	 * Return {@linkplain Vec3d[]} #1.
	 * 
	 * @return vector array #1.
	 */
	Vec3d[] getVectors1();

	/**
	 * Set {@linkplain Vec3d[]} #1.
	 * 
	 * @param vec vector array #1.
	 */
	Ports setVectors1(Vec3d[] vec);

	/**
	 * Return {@linkplain Entity} #1.
	 * 
	 * @return entity #1.
	 */
	Entity getEntity1();

	/**
	 * Set {@linkplain Entity} #1.
	 * 
	 * @param entity entity #1.
	 */
	Ports setEntity1(Entity entity);

	/**
	 * Return {@linkplain Entity} #2.
	 * 
	 * @return entity #2.
	 */
	Entity getEntity2();

	/**
	 * Set {@linkplain Entity} #2.
	 * 
	 * @param entity entity #2.
	 */
	Ports setEntity2(Entity entity);
	
	/**
	 * Return {@linkplain Entity[]} #1.
	 * 
	 * @return entity array #1.
	 */
	Entity[] getEntities1();

	/**
	 * Set {@linkplain Entity[]} #1.
	 * 
	 * @param entities entity array #1.
	 */
	Ports setEntities1(Entity[] entities);
	
	/**
	 * Return {@linkplain RayTraceResult} #1.
	 * 
	 * @return ray trace result #1.
	 */
	RayTraceResult getRayTraceResult1();

	/**
	 * Set {@linkplain RayTraceResult} #1.
	 * 
	 * @param result ray trace result #1.
	 */
	Ports setRayTraceResult1(RayTraceResult result);

	/**
	 * Return {@linkplain EffectInstance} #1.
	 * 
	 * @return effect instance #1.
	 */
	EffectInstance getEffectInstance1();

	/**
	 * Set {@linkplain EffectInstance} #1.
	 * 
	 * @param result effect instance #1.
	 */
	Ports setEffectInstance1(EffectInstance instance);
		
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
	Ports setWorld(World world);

	/**
	 * Set counter value.
	 * 
	 * @param value value to set a counter.
	 * 
	 * @return ports.
	 */
	Ports setCounter(int value);

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

	/**
	 * Get AABB.
	 * 
	 * @return AABB.
	 */
	AxisAlignedBB getAabb();

	/**
	 * Set AABB.
	 * 
	 * @param aabb AABB to set.
	 *
	 * @return ports.
	 */
	Ports setAabb(AxisAlignedBB aabb);

	/**
	 * Get matrix stack.
	 * 
	 * @return matrix stack.
	 */
	MatrixStack getMatrixStack();

	/**
	 * Set matrix stack .
	 * 
	 * @param ms matrix stack.
	 * 
	 * @return ports.
	 */
	Ports setMatrixStack(MatrixStack ms);

}
