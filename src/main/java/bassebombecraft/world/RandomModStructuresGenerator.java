package bassebombecraft.world;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MODID;

import java.util.List;
import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.config.ConfigUtils;
import bassebombecraft.config.StructureInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Implementation of the {@linkplain IWorldGenerator} interface which generates
 * structures packaged with the Mod.
 */
public class RandomModStructuresGenerator implements IWorldGenerator {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = RandomModStructuresGenerator.class.getSimpleName();

	/**
	 * Structure infos.
	 */
	List<StructureInfo> infos;

	/**
	 * Enable generator.
	 */
	boolean enabled;

	/**
	 * RandomModStructuresGenerator constructor.
	 */
	public RandomModStructuresGenerator() {
		Config configuration = getBassebombeCraft().getConfiguration();
		enabled = configuration.getBoolean(CONFIG_KEY + ".enabled");
		infos = ConfigUtils.createStructureInfosFromConfig(CONFIG_KEY + ".structures");
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator,
			AbstractChunkProvider chunkProvider) {

		// exit if not enabled
		if (!enabled)
			return;

		// get structure info
		if (infos == null)
			return;
		if (infos.isEmpty())
			return;

		// get random structure
		int index = random.nextInt(infos.size());
		StructureInfo info = infos.get(index);

		// determine if structure should be generated
		if (random.nextDouble() > info.getSpawnRate())
			return;

		// exit if incorrect dimension
		if (!generateInThisDimension(world, info))
			return;

		// exit if incorrect biome
		if (!shouldSpawnInBiome(chunkX, chunkZ, world, info))
			return;

		// generate
		generateStructure(random, chunkX, chunkZ, world, info);
	}

	/**
	 * Returns true if structure should spawn in the current biome.
	 * 
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @param info   structure info
	 * 
	 * @return true if structure should spawn in the current biome.
	 */
	boolean shouldSpawnInBiome(int chunkX, int chunkZ, World world, StructureInfo info) {
		String biome = info.getBiome();

		if (biome == null)
			return true;

		if (biome.isEmpty())
			return true;

		if (biome.equalsIgnoreCase("any"))
			return true;

		Biome currentBiome = world.getBiome(new BlockPos(chunkX, 0, chunkZ));
		String name = currentBiome.getCategory().getName();
		return biome.equalsIgnoreCase(name);
	}

	/**
	 * Return true if structure should be generated in the current dimension.
	 * 
	 * @param world world object.
	 * @param info  structure info.
	 * 
	 * @return true if structure should be generated in the current dimension.
	 */
	boolean generateInThisDimension(World world, StructureInfo info) {
		Dimension dimension = world.getDimension();

		if (dimension instanceof OverworldDimension)
			return true;

		return false;
	}

	/**
	 * Generate structure.
	 * 
	 * @param random random generator.
	 * @param chunkX chunk X
	 * @param chunkZ chunk Z
	 * @param world  world object.
	 * @param info   structure info to generate.
	 */
	void generateStructure(Random random, int chunkX, int chunkZ, World world, StructureInfo info) {
		// calculate position
		int x = chunkX * 16 + random.nextInt(16);
		int z = chunkZ * 16 + random.nextInt(16);
		int y = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x, z);

		BlockPos basePos = new BlockPos(x, y, z);
		String structureName = MODID + ":" + info.getName();
		TemplateUtils.load(world, structureName, basePos);
	}

}
