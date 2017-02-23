package bassebombecraft.world;

import net.minecraft.world.World;

/**
 * World utility class.
 */
public class WorldUtils {

	/**
	 * Return true if world is a client side (i.e. remote).
	 * 
	 * @param world
	 *            to test.
	 * @return true if world is a client side (i.e. remote).
	 */
	public static boolean isWorldAtClientSide(World world) {
		return world.isRemote;
	}

	/**
	 * Return true if world is a server side (i.e. not remote).
	 * 
	 * @param world
	 *            to test.
	 * @return true if world is a server side (i.e. not remote).
	 */
	public static boolean isWorldAtServerSide(World world) {
		return (!world.isRemote);
	}	
}
