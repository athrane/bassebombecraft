package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getUnresolvedInstance;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

/**
 * Configuration utility class.
 */
public class ConfigUtils {

	/**
	 * Create {@linkplain ParticleRenderingInfo} from a {@linkplain ParticlesConfig}
	 * object.
	 * 
	 * @param key configuration key to read configuration from.
	 * 
	 * @return single {@linkplain ParticleRenderingInfo}.
	 */
	public static ParticleRenderingInfo createInfoFromConfig(ParticlesConfig config) {
		String unresolvedType = config.type.get();
		int number = config.number.get();
		int duration = config.duration.get();
		double colorR = config.r.get();
		double colorG = config.g.get();
		double colorB = config.b.get();
		double speed = config.speed.get();
		ParticleRenderingInfo info = getUnresolvedInstance(unresolvedType, number, duration, (float) colorR,
				(float) colorG, (float) colorB, speed);
		return info;
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
	@Deprecated
	public static int resolveCoolDown(String key, int defaultValue) {

		// define configuration path in .TOML file
		String tomlPath = key + ".cooldown";

		// get optional value from forge config
		// please notice: config.getOptionalInt(..) is used due to a type cast bug
		UnmodifiableConfig config = getBassebombeCraft().getTomlConfiguration();
		if (config.contains(tomlPath))
			return getInt(tomlPath);

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
	@Deprecated	
	public static String resolveTooltip(String key, String defaultValue) {

		// define configuration path in .TOML file
		String tomlPath = key + ".tooltip";

		// get optional value from forge config
		UnmodifiableConfig config = getBassebombeCraft().getTomlConfiguration();
		if (config.contains(tomlPath)) {
			ConfigValue<String> configValue = config.get(tomlPath);
			return configValue.get();
		}

		// return default value if path is undefined
		return defaultValue;
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
