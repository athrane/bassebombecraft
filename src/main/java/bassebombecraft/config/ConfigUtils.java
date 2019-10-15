package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.CONFIG_FILE_POSTFIX;
import static bassebombecraft.ModConstants.INTERNAL_CONFIG_FILE_NAME;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.file.FileUtils;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

/**
 * Configuration utility class.
 */
public class ConfigUtils {

	/**
	 * Rendering options for saved configuration file.
	 */
	static final ConfigRenderOptions OPTIONS = ConfigRenderOptions.defaults().setComments(true).setOriginComments(false)
			.setFormatted(true);

	/**
	 * Create array with single {@linkplain ParticleRenderingInfo} from
	 * configuration.
	 * 
	 * @param key configuration key to read configuration from.
	 * @return array with single {@linkplain ParticleRenderingInfo}.
	 */
	public static ParticleRenderingInfo[] createFromConfig(String key) {
		Config configuration = getBassebombeCraft().getConfiguration();
		String particleTypeName = configuration.getString(key + ".Particles.Type");
		ResourceLocation key2 = new ResourceLocation(particleTypeName.toLowerCase());
		Optional<ParticleType<? extends IParticleData>> particleType = Registry.PARTICLE_TYPE.getValue(key2);
		BasicParticleType castParticleType = (BasicParticleType) particleType.get(); // type cast
		int number = configuration.getInt(key + ".Particles.Number");
		int duration = configuration.getInt(key + ".Particles.Duration");
		double colorR = configuration.getDouble(key + ".Particles.Color.R");
		double colorG = configuration.getDouble(key + ".Particles.Color.G");
		double colorB = configuration.getDouble(key + ".Particles.Color.B");
		double speed = configuration.getDouble(key + ".Particles.Speed");
		ParticleRenderingInfo mist = getInstance(castParticleType, number, duration, (float) colorR, (float) colorG,
				(float) colorB, speed);
		return new ParticleRenderingInfo[] { mist };
	}

	/**
	 * Create list of {@linkplain StructureInfo} from configuration file.
	 * 
	 * @param key configuration key to read configuration from.
	 * @return list of {@linkplain StructureInfo} from configuration file.
	 */
	public static List<StructureInfo> createStructureInfosFromConfig(String key) {
		Config configuration = getBassebombeCraft().getConfiguration();
		List<StructureInfo> infos = new ArrayList<StructureInfo>();
		for (Config particularConfig : configuration.getConfigList(key)) {
			StructureInfo particularUser = new StructureInfo(particularConfig);
			infos.add(particularUser);
		}
		return infos;
	}

	/**
	 * Resolve entity cooldown value from configuration. If no key is defined then
	 * the default value is returned.
	 * 
	 * please notice: config.getOptionalInt(..) is used due to a type cast bug in
	 * the forge configuration class.
	 * 
	 * @param key          configuration key.
	 * @param defaultValue default cooldown value.
	 * 
	 * @return resolve cooldown value from configuration.
	 */
	public static int resolveCoolDown(String key, int defaultValue) {

		// define configuration path in .TOML file
		String tomlPath = key + ".cooldown";

		// get optional value from forge config
		// please notice: config.getOptionalInt(..) is used due to a type cast bug
		UnmodifiableConfig config = getBassebombeCraft().getTomlConfiguration();
		if (config.contains(tomlPath))
			return getInt(tomlPath);

		// get value for typesafe config
		Config configuration = getBassebombeCraft().getConfiguration();
		String path = key + ".Cooldown";

		// return value if path is defined
		if (configuration.hasPath(path))
			return configuration.getInt(path);

		// return default value if path is undefined
		return defaultValue;
	}

	/**
	 * Resolve entity tooltip from configuration. If no key is defined then the
	 * default value is returned.
	 * 
	 * @param key          configuration key.
	 * @param defaultValue default cooldown value.
	 * 
	 * @return resolve cooldown value from configuration.
	 */
	public static String resolveTooltip(String key, String defaultValue) {

		// define configuration path in .TOML file
		String tomlPath = key + ".tooltip";

		// get optional value from forge config
		UnmodifiableConfig config = getBassebombeCraft().getTomlConfiguration();
		if (config.contains(tomlPath)) {
			ConfigValue<String> configValue = config.get(tomlPath);
			return configValue.get();
		}

		Config configuration = getBassebombeCraft().getConfiguration();
		String path = key + ".Tooltip";

		// return default value if path is undefined
		if (!configuration.hasPath(path))
			return defaultValue;

		return configuration.getString(path);
	}

	/**
	 * Load configuration.
	 * 
	 * If external configuration file doesn't exist then it is created. If an
	 * external configuration file exist then it is loaded.
	 * 
	 * @param configDirectory mod configuration directory,
	 */
	public static Config loadConfig(File configDirectory) {
		Logger logger = getBassebombeCraft().getLogger();

		// create external file name
		String externalFileName = new StringBuilder().append(MODID).append("-").append(VERSION)
				.append(CONFIG_FILE_POSTFIX).toString();
		File externalFile = new File(configDirectory, externalFileName);

		// if no external file exists then create it
		if (!externalFile.exists()) {
			logger.info("No configuration file found, will create one at: " + externalFile.getAbsolutePath());

			// load from internal configuration file
			Config config = ConfigFactory.load(INTERNAL_CONFIG_FILE_NAME);
			String content = config.root().render(OPTIONS);

			// save as JSON
			FileUtils.saveJsonFile(content, externalFile);

		}

		// load configuration
		logger.info("Loading configuration file from: " + externalFile.getAbsolutePath());

		return ConfigFactory.parseFile(externalFile);
	}

	/**
	 * Get integer value from .TOML configuration file.
	 * 
	 * @param path configuration path in .TOML configuration file.
	 * 
	 * @return integer value from .TOML configuration file
	 */
	public static int getInt(String path) {
		UnmodifiableConfig config = getBassebombeCraft().getTomlConfiguration();
		try {
			IntValue configValue = config.get(path);
			Integer retval = configValue.get();
			return retval;
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
			throw e;
		}
	}

}
