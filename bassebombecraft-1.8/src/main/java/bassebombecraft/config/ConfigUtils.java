package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import com.typesafe.config.Config;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.util.EnumParticleTypes;

/**
 * Configuration utility class.
 */
public class ConfigUtils {

	/**
	 * Create array with single {@linkplain ParticleRenderingInfo} from
	 * configuration.
	 * 
	 * @param key
	 *            configuration key to read configuration from.
	 * @return array with single {@linkplain ParticleRenderingInfo}.
	 */
	public static ParticleRenderingInfo[] createFromConfig(String key) {
		Config configuration = getBassebombeCraft().getConfiguration();
		EnumParticleTypes particleType = EnumParticleTypes
				.getByName(configuration.getString(key+".Particles.Type"));
		int number = configuration.getInt(key+".Particles.Number");
		int duration = configuration.getInt(key+".Particles.Duration");
		double colorR = configuration.getDouble(key+".Particles.Color.R");
		double colorG = configuration.getDouble(key+".Particles.Color.G");
		double colorB = configuration.getDouble(key+".Particles.Color.B");
		double speed = configuration.getDouble(key+".Particles.Speed");
		ParticleRenderingInfo mist = getInstance(particleType, number, duration, (float) colorR, (float) colorG,
				(float) colorB, speed);
		return new ParticleRenderingInfo[] { mist };

	}
	
	/**
	 * Resolve entity cooldown value from configuration. If no key is defined then the
	 * default value is returned.
	 * 
	 * @param key
	 *            configuration key.
	 * @param defaultValue
	 *            default cooldown value.
	 * @return resolve cooldown value from configuration.
	 */
	public static int resolveCoolDown(String key, int defaultValue) {
		
		Config configuration = getBassebombeCraft().getConfiguration();
		String path = key+".Cooldown";

		// return default value if path is undefined
		if(!configuration.hasPath(path)) return defaultValue;
		
		return configuration.getInt(path);
	}
	
}
