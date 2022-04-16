package bassebombecraft.operator;

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

/**
 * Collection of ports which provides input and output from operators.
 */
public interface Ports {

	/**
	 * Return true if debug features should be enabled.
	 * 
	 * @return true if debug features should be enabled.
	 */
	boolean isDebugEnabled();

	/**
	 * Enable debug features.
	 * 
	 * @return ports.
	 */
	Ports enableDebug();
	
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
	 * 
	 * @return true if operation was successful.
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
	 * Return {@linkplain LivingEntity[]} #1.
	 * 
	 * @return living entity array #1.
	 */
	LivingEntity[] getLivingEntities1();

	/**
	 * Set {@linkplain LivingEntity[]} #1.
	 * 
	 * @param living entities entity array #1.
	 */
	Ports setLivingEntities1(LivingEntity[] entities);
	
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
	 * Get {@linkplain ItemStack} #1.
	 * 
	 * @return item stack #1.
	 */
	ItemStack getItemStack1();

	/**
	 * Set {@linkplain ItemStack} #1.
	 * 
	 * @return ports.
	 */
	Ports setItemStack1(ItemStack itemStack);
	
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
	 * Get integer  #1.
	 * 
	 * @return integer #1 .
	 */
	int getInteger1();

	/**
	 * Set integer #1.
	 * 
	 * @return ports.
	 */
	Ports setInteger1(int value);

	/**
	 * Return {@linkplain Vector2f} #1.
	 * 
	 * @return vector #1.
	 */
	Vec2 getVector2f1();

	/**
	 * Set {@linkplain Vector2f} #1.
	 * 
	 * @param vec vector #1.
	 */
	Ports setVector2f1(Vec2 vec);
	
	/**
	 * Return {@linkplain Vector3d} #1.
	 * 
	 * @return vector #1.
	 */
	Vec3 getVector1();

	/**
	 * Set {@linkplain Vector3d} #1.
	 * 
	 * @param vec vector #1.
	 */
	Ports setVector1(Vec3 vec);

	/**
	 * Return {@linkplain Vector3d[]} #1.
	 * 
	 * @return vector array #1.
	 */
	Vec3[] getVectors1();

	/**
	 * Set {@linkplain Vector3d[]} #1.
	 * 
	 * @param vec vector array #1.
	 */
	Ports setVectors1(Vec3[] vec);

	/**
	 * Set {@linkplain Vector3d[]} #2.
	 * 
	 * @param vec vector array #2.
	 */
	Ports setVectors2(Vec3[] vec);

	/**
	 * Return {@linkplain Vector3d[]} #2.
	 * 
	 * @return vector array #2.
	 */
	Vec3[] getVectors2();

	/**
	 * Return {@linkplain Color4f} #1.
	 * 
	 * @return color #1.
	 */
	Color4f getColor4f1();

	/**
	 * Set {@linkplain Color4f} #1.
	 * 
	 * @param color color #1.
	 */
	Ports setColor4f1(Color4f color);

	/**
	 * Return {@linkplain Color4f} #2.
	 * 
	 * @return color #2.
	 */
	Color4f getColor4f2();

	/**
	 * Set {@linkplain Color4f} #2.
	 * 
	 * @param color color #2.
	 */
	Ports setColor4f2(Color4f color);

	/**
	 * Return {@linkplain RayTraceResult} #1.
	 * 
	 * @return ray trace result #1.
	 */
	HitResult getRayTraceResult1();

	/**
	 * Set {@linkplain RayTraceResult} #1.
	 * 
	 * @param result ray trace result #1.
	 */
	Ports setRayTraceResult1(HitResult result);

	/**
	 * Return {@linkplain EffectInstance} #1.
	 * 
	 * @return effect instance #1.
	 */
	MobEffectInstance getEffectInstance1();

	/**
	 * Set {@linkplain EffectInstance} #1.
	 * 
	 * @param result effect instance #1.
	 */
	Ports setEffectInstance1(MobEffectInstance instance);

	/**
	 * Return world.
	 * 
	 * @return world.
	 */
	Level getWorld();

	/**
	 * Set world.
	 * 
	 * @param world world object.
	 */
	Ports setWorld(Level world);

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
	 * Get AABB #1.
	 * 
	 * @return AABB.
	 */
	AABB getAabb1();

	/**
	 * Set AABB #1.
	 * 
	 * @param aabb AABB to set.
	 *
	 * @return ports.
	 */
	Ports setAabb1(AABB aabb);

	/**
	 * Get damage source #1.
	 * 
	 * @return matrix stack.
	 */
	DamageSource getDamageSource1();

	/**
	 * Set damage source #1.
	 * 
	 * @param ds damage source .
	 * 
	 * @return ports.
	 */
	Ports setDamageSource1(DamageSource ds);

}
