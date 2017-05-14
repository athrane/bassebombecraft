package bassebombecraft.config;

import com.typesafe.config.Config;

/**
 * Structure info object read from Configuration API.
 */
public class StructureInfo {

	/**
	 * Structure name.
	 */
	String name;

	/**
	 * Structure spawn rate.
	 */
	double spawnRate;

	/**
	 * Biome where structure can spawn.
	 */
	String biome;

	/*
	 * StructureInfo constructor.
	 */
	public StructureInfo(final Config params) {
		super();
		name = params.getString("name");
		spawnRate = params.getDouble("spawnRate");
		biome = params.getString("biome");
	}

	public String getName() {
		return name;
	}

	public double getSpawnRate() {
		return spawnRate;
	}

	public String getBiome() {
		return biome;
	}

}
