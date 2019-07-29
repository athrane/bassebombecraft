package bassebombecraft.projectile.action;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns many
 * creepers.
 */
public class CreeperApocalypse implements ProjectileAction {

	static final int NUMBER_CREEPER = 100;
	static final float PITCH = 0.0F;
	static final int EFFECT_DURATION = 200; // Measured in ticks
	private static final int AMPLIFIER = 255;
	private static final int CREEPER_FUSED = 1;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {

		for (int i = 0; i < NUMBER_CREEPER; i++) {
			CreeperEntity entity = EntityType.CREEPER.create(world);

			// set powered
			entity.setCreeperState(CREEPER_FUSED);

			// set position
			entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
					PITCH);

			// prime
			entity.ignite();

			// add potion effect
			entity.addPotionEffect(createEffect());

			// spawn
			world.addEntity(entity);
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(Effects.RESISTANCE, EFFECT_DURATION, AMPLIFIER);
	}

}
