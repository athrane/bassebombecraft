package bassebombecraft.world;

import java.util.Optional;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;

/**
 * World utility class.
 */
public class WorldUtils {

	/**
	 * Return true if world is a client side (i.e. remote).
	 * 
	 * @param world to test.
	 * @return true if world is a client side (i.e. remote).
	 */
	public static boolean isWorldAtClientSide(World world) {
		return world.isRemote;
	}

	/**
	 * Return true if world is a server side (i.e. not remote).
	 * 
	 * @param world to test.
	 * @return true if world is a server side (i.e. not remote).
	 */
	public static boolean isWorldAtServerSide(World world) {
		return (!world.isRemote);
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
	 * Add lightning bolt to the world if the world is either a
	 * {@linkplain ServerWorld} or a {@linkplain ClientWorld}.
	 * 
	 * @param entity lightning bolt which is added to the world.
	 * @param world  the world where the bolt is added to.
	 */
	public static void addLightning(LightningBoltEntity entity, World world) {

		if (isTypeServerWorld(world)) {

			// type cast
			ServerWorld serverWorld = (ServerWorld) world;

			// add lightning
			serverWorld.addLightningBolt(entity);			
			return;
		}
		
		if (isTypeClientWorld(world)) {

			// type cast
			ClientWorld serverWorld = (ClientWorld) world;

			// add lightning
			serverWorld.addLightning(entity);			
			return;
		}
		
	}
}
