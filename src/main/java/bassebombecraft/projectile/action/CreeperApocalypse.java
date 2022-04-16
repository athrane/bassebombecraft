package bassebombecraft.projectile.action;

import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns many
 * creepers.
 */
public class CreeperApocalypse implements ProjectileAction {

	static final int NUMBER_CREEPER = 100;
	static final float PITCH = 0.0F;
	static final int EFFECT_DURATION = 200; // Measured in ticks
	static final int AMPLIFIER = 255;
	static final int CREEPER_FUSED = 1;
	static final int SPAWN_SIZE = 1;

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {

		for (int i = 0; i < NUMBER_CREEPER; i++) {
			Creeper entity = EntityType.CREEPER.create(world);

			// set powered
			entity.setSwellDir(CREEPER_FUSED);
			
			// set position
			setRandomSpawnPosition(projectile.blockPosition(), projectile.yRot, SPAWN_SIZE, entity);

			// prime
			entity.ignite();

			// add potion effect
			entity.addEffect(createEffect());

			// spawn
			world.addFreshEntity(entity);
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, AMPLIFIER);
	}

}
