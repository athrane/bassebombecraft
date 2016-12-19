package bassebombecraft.projectile.action;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
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
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		for (int i = 0; i < NUMBER_CREEPER; i++) {
			EntityCreeper creeper = new EntityCreeper(world);

			// set powered
			creeper.setCreeperState(CREEPER_FUSED);

			// set position
			creeper.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
					PITCH);

			// prime
			creeper.ignite();

			// add potion effect
			creeper.addPotionEffect(createEffect());

			world.spawnEntity(creeper);
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	PotionEffect createEffect() {
		return new PotionEffect(MobEffects.RESISTANCE, EFFECT_DURATION, AMPLIFIER);
	}

}
