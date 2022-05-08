package bassebombecraft.world;

import java.util.Optional;

import bassebombecraft.ModConstants;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import static net.minecraft.sounds.SoundEvents.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * World utility class.
 */
public class WorldUtils {

	/**
	 * Sound volume.
	 */
	static final float SOUND_VOLUME = 0.5F;
	
	/**
	 * Sound pitch.
	 */	
	static final float SOUND_PITCH = 1.0F;

	/**
	 * Return true if world is at logical client side (i.e. remote).
	 * 
	 * @param world to test.
	 * 
	 * @return true if world is at logical client side (i.e. remote).
	 */
	public static boolean isLogicalClient(Level world) {
		return world.isClientSide();
	}
	
	/**
	 * Return true if world is at logical server side (i.e. not remote).
	 * 
	 * @param world to test.
	 * 
	 * @return true if world is at logical server side (i.e. not remote).
	 */
	public static boolean isLogicalServer(Level world) {
		return (!world.isClientSide());
	}

	/**
	 * Return true if world is at logical client side (i.e. remote).
	 * 
	 * @param entity embedded world in entity is tested.
	 * 
	 * @return true if world is at logical client side (i.e. remote).
	 * 
	 * @throws IllegalArgumentException if parameter is undefined.
	 */
	public static boolean isLogicalClient(Entity entity) {
		return isLogicalClient(entity.getCommandSenderWorld());
	}
	
	/**
	 * Return true if world is at logical server side (i.e. not remote).
	 * 
	 * @param entity embedded world in entity is tested.
	 * 
	 * @return true if world is at logical server side (i.e. not remote).
	 */
	public static boolean isLogicalServer(Entity entity) {
		return isLogicalServer(entity.getCommandSenderWorld());
	}
	
	/**
	 * Return true if entity is a {@linkplain ServerWorld}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain ServerWorld}.
	 */
	public static boolean isTypeServerWorld(Level world) {
		Optional<Level> ow = Optional.ofNullable(world);
		if (ow.isPresent())
			return ow.get() instanceof Level;
		return false;
	}

	/**
	 * Return true if entity is a {@linkplain ClientWorld}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain ClientWorld}.
	 */
	public static boolean isTypeClientWorld(Level world) {
		Optional<Level> ow = Optional.ofNullable(world);
		if (ow.isPresent())
			return ow.get() instanceof ClientLevel;
		return false;
	}

	/**
	 * Add lightning at block position.
	 * 
	 * @param world world.
	 * @param pos   block position where lightning is added.
	 */
	public static void addLightningAtBlockPos(Level world, BlockPos pos) {
		
		// create lightning bolt entity
        LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
        lightningboltentity.moveTo(Vec3.atBottomCenterOf(pos));
        lightningboltentity.setVisualOnly(ModConstants.LIGHTNING_BOLT_NOT_EFFECT_ONLY);
        world.addFreshEntity(lightningboltentity);

        // play sound
        lightningboltentity.playSound(LIGHTNING_BOLT_THUNDER, SOUND_VOLUME, SOUND_PITCH);       
	}
	
}
