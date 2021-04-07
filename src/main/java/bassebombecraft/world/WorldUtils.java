package bassebombecraft.world;

import java.util.Optional;

import bassebombecraft.ModConstants;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import static net.minecraft.util.SoundEvents.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
	public static boolean isLogicalClient(World world) {
		return world.isRemote();
	}
	
	/**
	 * Return true if world is at logical server side (i.e. not remote).
	 * 
	 * @param world to test.
	 * 
	 * @return true if world is at logical server side (i.e. not remote).
	 */
	public static boolean isLogicalServer(World world) {
		return (!world.isRemote());
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
		return isLogicalClient(entity.getEntityWorld());
	}
	
	/**
	 * Return true if world is at logical server side (i.e. not remote).
	 * 
	 * @param entity embedded world in entity is tested.
	 * 
	 * @return true if world is at logical server side (i.e. not remote).
	 */
	public static boolean isLogicalServer(Entity entity) {
		return isLogicalServer(entity.getEntityWorld());
	}
	
	/**
	 * Return true if entity is a {@linkplain ServerWorld}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain ServerWorld}.
	 */
	public static boolean isTypeServerWorld(World world) {
		Optional<World> ow = Optional.ofNullable(world);
		if (ow.isPresent())
			return ow.get() instanceof World;
		return false;
	}

	/**
	 * Return true if entity is a {@linkplain ClientWorld}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain ClientWorld}.
	 */
	public static boolean isTypeClientWorld(World world) {
		Optional<World> ow = Optional.ofNullable(world);
		if (ow.isPresent())
			return ow.get() instanceof ClientWorld;
		return false;
	}

	/**
	 * Add lightning at block position.
	 * 
	 * @param world world.
	 * @param pos   block position where lightning is added.
	 */
	public static void addLightningAtBlockPos(World world, BlockPos pos) {
		
		// create lightning bolt entity
        LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
        lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(pos));
        lightningboltentity.setEffectOnly(ModConstants.LIGHTNING_BOLT_NOT_EFFECT_ONLY);
        world.addEntity(lightningboltentity);

        // play sound
        lightningboltentity.playSound(ENTITY_LIGHTNING_BOLT_THUNDER, SOUND_VOLUME, SOUND_PITCH);       
	}
	
}
