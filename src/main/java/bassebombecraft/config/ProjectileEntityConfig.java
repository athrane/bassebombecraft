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
	public ForgeConfigSpec.DoubleValue gravity;
	public ParticlesConfig particles;

	/**
	 * Constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder    configuration spec builder.
	 * @param name       item name.
	 * @param inaccuracy projectile inaccuracy when shoot.
	 * @param force      projectile force when shoot. Meaningful range = [0..10].
	 * @param damage     projectile damage when hitting a mob.
	 * @param gravity    projectile gravity pull. Meaningful range = [0..1].
	 * @param splParticles   {@linkplain ParticlesConfig} supplier.
	 */
	public ProjectileEntityConfig(Builder builder, String name, double inaccuracy, double force, double damage,
			double gravity, Supplier<ParticlesConfig> splParticles) {
		builder.comment(name + " settings").push(name);
		this.inaccuracy = builder.comment("Projectile inaccuracy when fired.").defineInRange("inaccuracy", inaccuracy,
				0, Double.MAX_VALUE);
		this.force = builder.comment("Projectile force when fired.").defineInRange("force", force, 0, 10.0D);
		this.damage = builder.comment("Projectile damage when hitting a mob.").defineInRange("damage", damage, 0,
				Double.MAX_VALUE);
		this.gravity = builder.comment("Projectile gravity applied every tick.").defineInRange("gravity", gravity, 0,
				1.0D);
		this.particles = splParticles.get();
		builder.pop();
	}

	/**
	 * Factory method.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param inaccuracy   projectile inaccuracy when shoot.
	 * @param force        projectile force when shoot. Meaningful range = [0..10].
	 * @param damage       projectile damage when hitting a mob.
	 * @param gravity      projectile gravity pull. Meaningful range = [0..1].
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 */
	public static ProjectileEntityConfig getInstance(Builder builder, String name, double inaccuracy, double force,
			double damage, double gravity, Supplier<ParticlesConfig> splParticles) {
		return new ProjectileEntityConfig(builder, name, inaccuracy, force, damage, gravity, splParticles);
	}

}
