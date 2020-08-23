package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Configuration utility class.
 */
public class ConfigUtils {

	/**
	 * Create array with single {@linkplain ParticleRenderingInfo} from a
	 * {@linkplain ParticlesConfig} object.
	 * 
	 * @param key configuration key to read configuration from.
	 * 
	 * @return array with single {@linkplain ParticleRenderingInfo}.
	 */
	public static ParticleRenderingInfo[] createFromConfig(ParticlesConfig config) {
		ParticleType<?> particleType = resolveParticleType(config);
		BasicParticleType castParticleType = (BasicParticleType) particleType;
		int number = config.number.get();
		int duration = config.duration.get();
		double colorR = config.r.get();
		double colorG = config.g.get();
		double colorB = config.b.get();
		double speed = config.speed.get();
		ParticleRenderingInfo info = getInstance(castParticleType, number, duration, (float) colorR, (float) colorG,
				(float) colorB, speed);
		return new ParticleRenderingInfo[] { info };
	}

	/**
	 * Resolve particle type from particle configuration object.
	 * 
	 * @param config particle configuration object.
	 * 
	 * @return particle type object.
	 */
	static ParticleType<?> resolveParticleType(ParticlesConfig config) {

		// get particle type name
		String name = config.type.get();

		// resolve vanilla particle
		ResourceLocation key2 = new ResourceLocation(name.toLowerCase());
		return ForgeRegistries.PARTICLE_TYPES.getValue(key2);
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
