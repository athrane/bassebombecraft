package bassebombecraft.projectile.action;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
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

	@Override
	public void execute(EntityThrowable projectile, World world, MovingObjectPosition movObjPos) {

		for (int i = 0; i < NUMBER_CREEPER; i++) {
			EntityCreeper creeper = new EntityCreeper(world);

			// set powered
			creeper.getDataWatcher().updateObject(17, Byte.valueOf((byte) 1));

			// set position
			creeper.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
					PITCH);

			// prime
			creeper.ignite();

			// add potion effect
			creeper.addPotionEffect(createEffect());

			world.spawnEntityInWorld(creeper);
		}
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	PotionEffect createEffect() {
		return new PotionEffect(Potion.resistance.id, EFFECT_DURATION, AMPLIFIER);
	}

}
