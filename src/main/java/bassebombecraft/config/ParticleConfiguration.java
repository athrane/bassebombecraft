package bassebombecraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining particle rendering information in configuration files.
 */
public class ParticleConfiguration {

	public ForgeConfigSpec.ConfigValue<String> type;
	public ForgeConfigSpec.IntValue number;
	public ForgeConfigSpec.IntValue duration;
	public ForgeConfigSpec.DoubleValue speed;
	public ForgeConfigSpec.DoubleValue r;
	public ForgeConfigSpec.DoubleValue g;
	public ForgeConfigSpec.DoubleValue b;

	/**
	 * ParticleConfiguration constructor.
	 * 
	 * @param builder  configuration spec builder.
	 * @param type     particle type.
	 * @param number   number of particle to spawn.
	 * @param duration particle duration.
	 * @param speed    particle speed.
	 * @param r        r-color component.
	 * @param g        g-color component.
	 * @param b        b-color component.
	 */
	public ParticleConfiguration(Builder builder, String type, int number, int duration, double speed, double r,
			double g, double b) {
		builder.comment("Particle settings").push("Particles");
		this.type = builder.comment(
				"Particle type. Legal particle type names can be seen in the net.minecraft.particles.ParticleTypes class.")
				.define("type", type);
		this.number = builder.comment("Number of particle to spawn per update.").defineInRange("number", number, 0,
				Integer.MAX_VALUE);
		this.duration = builder.comment("Duration of the particles in game ticks.").defineInRange("duration", duration,
				0, Integer.MAX_VALUE);
		this.duration = builder.comment("Duration of the particles in game ticks.").defineInRange("duration", duration,
				0, Integer.MAX_VALUE);
		this.speed = builder.comment("Speed of the spawned particles.").defineInRange("speed", speed, 0,
				Double.MAX_VALUE);
		this.r = builder.comment("R-component of particle RGB color.").defineInRange("r", r, 0, 1);
		this.g = builder.comment("G-component of particle RGB color.").defineInRange("g", g, 0, 1);
		this.b = builder.comment("B-component of particle RGB color.").defineInRange("b", b, 0, 1);
		builder.pop();
	}

	/**
	 * ParticleConfiguration factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param type     particle type.
	 * @param number   number of particle to spawn.
	 * @param duration particle duration.
	 * @param speed    particle speed.
	 * @param r        r-color component.
	 * @param g        g-color component.
	 * @param b        b-color component.
	 * 
	 * @return particle configuration.
	 */
	public static ParticleConfiguration getInstance(Builder builder, String type, int number, int duration,
			double speed, double r, double g, double b) {
		return new ParticleConfiguration(builder, type, number, duration, speed, r, g, b);
	}

}
