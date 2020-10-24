package bassebombecraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining particle rendering information in configuration files.
 */
public class ParticlesConfig {

	public ForgeConfigSpec.ConfigValue<String> type;
	public ForgeConfigSpec.IntValue number;
	public ForgeConfigSpec.IntValue duration;
	public ForgeConfigSpec.DoubleValue speed;
	public ForgeConfigSpec.DoubleValue r;
	public ForgeConfigSpec.DoubleValue g;
	public ForgeConfigSpec.DoubleValue b;

	/**
	 * ParticleConfig constructor.
	 * 
	 * Adds a subsection named ".ParticleInfo".
	 * 
	 * @param builder  configuration spec builder.
	 * @param type     particle type.
	 * @param number   number of particle to spawn.
	 * @param duration particle duration in game ticks, i.e. the particle max age.
	 *                 Duration can be negative for continuous spawning of the
	 *                 particle. The age of a particle is the absolute value of the
	 *                 duration, i.e. the negative value results in the same max age
	 *                 as a positive number.
	 * @param speed    particle speed.
	 * @param r        r-color component.
	 * @param g        g-color component.
	 * @param b        b-color component.
	 */
	public ParticlesConfig(Builder builder, String type, int number, int duration, double speed, double r, double g,
			double b) {
		builder.comment("Particles settings").push("Particles");
		this.type = builder.comment(
				"Particle type. Legal vanila particle type names can be seen in the net.minecraft.particles.ParticleTypes class or bassebombecraft particles in bassebombecraft.client.particles.RegisteredParticles")
				.define("type", type);
		this.number = builder.comment("Number of particle to spawn per update.").defineInRange("number", number, 0,
				Integer.MAX_VALUE);
		this.duration = builder.comment(
				"Duration of the particles in game ticks. i.e. the particle max age. Duration can be negative for continuous spawning of the particle. The age of a particle is the absolute value of the duration, i.e. the negative value results in the same max age as a positive number.")
				.defineInRange("duration", duration, Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.speed = builder.comment("Speed of the spawned particles.").defineInRange("speed", speed, 0,
				Double.MAX_VALUE);
		this.r = builder.comment("R-component of particle RGB color.").defineInRange("r", r, 0, 1);
		this.g = builder.comment("G-component of particle RGB color.").defineInRange("g", g, 0, 1);
		this.b = builder.comment("B-component of particle RGB color.").defineInRange("b", b, 0, 1);
		builder.pop();
	}

	/**
	 * ParticlesConfig factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param type     particle type.
	 * @param number   number of particle to spawn.
	 * @param duration particle duration in game ticks, i.e. the particle max age.
	 *                 Duration can be negative for continuous spawning of the
	 *                 particle. The age of a particle is the absolute value of the
	 *                 duration, i.e. the negative value results in the same max age
	 *                 as a positive number.
	 * @param speed    particle speed.
	 * @param r        r-color component.
	 * @param g        g-color component.
	 * @param b        b-color component.
	 * 
	 * @return Particles configuration.
	 */
	public static ParticlesConfig getInstance(Builder builder, String type, int number, int duration, double speed,
			double r, double g, double b) {
		return new ParticlesConfig(builder, type, number, duration, speed, r, g, b);
	}

}
