package bassebombecraft.config;

import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining projectile entity information in configuration files.
 */
public class ProjectileEntityConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.DoubleValue inaccuracy;
	public ForgeConfigSpec.DoubleValue force;
	public ForgeConfigSpec.DoubleValue damage;
	public ParticlesConfig particles;

	/**
	 * Constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder    configuration spec builder.
	 * @param name       item name.
	 * @param inaccuracy projectile inaccuracy when shoot.
	 * @param force      projectile force when shoot.
	 * @param damage     projectile damage when hitting a mob..
	 * @param supplier   {@linkplain ParticlesConfig} supplier.
	 */
	public ProjectileEntityConfig(Builder builder, String name, double inaccuracy, double force, double damage,
			Supplier<ParticlesConfig> supplier) {
		builder.comment(name + " settings").push(name);
		this.inaccuracy = builder.comment("Projectile inaccuracy when fired.").defineInRange("inaccuracy", inaccuracy,
				0, Double.MAX_VALUE);
		this.force = builder.comment("Projectile force when fired.").defineInRange("force", force, 0, Double.MAX_VALUE);
		this.damage = builder.comment("Projectile damage when hitting a mob.").defineInRange("damage", damage, 0,
				Double.MAX_VALUE);
		this.particles = supplier.get();
		builder.pop();
	}

	/**
	 * Factory method.
	 * 
	 * @param builder    configuration spec builder.
	 * @param name       item name.
	 * @param inaccuracy projectile inaccuracy when shoot.
	 * @param force      projectile force when shoot.
	 * @param damage     projectile damage when hitting a mob..
	 * @param supplier   {@linkplain ParticlesConfig} supplier.
	 */
	public static ProjectileEntityConfig getInstance(Builder builder, String name, double inaccuracy, double force,
			double damage, Supplier<ParticlesConfig> supplier) {
		return new ProjectileEntityConfig(builder, name, inaccuracy, force, damage, supplier);
	}

}
