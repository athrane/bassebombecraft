package bassebombecraft.world;

import static bassebombecraft.ModConstants.MODID;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Implementation of the {@linkplain IWorldGenerator} interface which generates
 * structures packaged with the Mod.
 */
public class RandomModStructuresGenerator implements IWorldGenerator {

	static final float SPAWN_RATE = 0.01F;
	static final String STRUCTURE_TOWER_NAME = MODID + ":tower";
	static final ResourceLocation STRUCTURE_TOWER = new ResourceLocation(STRUCTURE_TOWER_NAME);

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {

		// determine if structure should be generated
		if (random.nextFloat() > SPAWN_RATE)
			return;

		// exit if incorrect dimension
		if (!generateInThisDimension(world))
			return;

		// generate
		generateStructure(random, chunkX, chunkZ, world);

	}

	/**
	 * Return true if structure should be generated in the current dimension.
	 * 
	 * @param world
	 *            world object.
	 * 
	 * @return true if structure should be generated in the current dimension.
	 */
	boolean generateInThisDimension(World world) {
		switch (world.provider.getDimensionType()) {

		case NETHER:
			return false;

		case THE_END:
			return false;

		case OVERWORLD:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Generate structure.
	 * 
	 * @param random
	 *            random generator.
	 * @param chunkX
	 *            chunk X
	 * @param chunkZ
	 *            chunk Z
	 * @param world
	 *            world object.
	 */
	void generateStructure(Random random, int chunkX, int chunkZ, World world) {
		// calculate position
		int x = chunkX * 16 + random.nextInt(16);
		int z = chunkZ * 16 + random.nextInt(16);
		int y = world.getHeight(x, z);

		BlockPos basePos = new BlockPos(x, y, z);
		TemplateUtils.load(world, STRUCTURE_TOWER_NAME, basePos);
	}

}
