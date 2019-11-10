package bassebombecraft.config;

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

	/**
	 * Defines if structure is singleton.
	 */
	boolean singleton;

	/*
	 * StructureInfo constructor.
	 */
	public StructureInfo() {
		// final Config params
		super();
		// name = params.getString("name");
		// spawnRate = params.getDouble("spawnRate");
		// biome = params.getString("biome");
		// singleton = params.getBoolean("singleton");
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

	public boolean isSingleton() {
		return singleton;
	}
}
