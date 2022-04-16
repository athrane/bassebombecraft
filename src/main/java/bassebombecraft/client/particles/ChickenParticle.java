package bassebombecraft.client.particles;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Chicken particle.
 * 
 * Supports animation based on age.
 * 
 * The set of animation frames are defined in the the .json file.
 */
public class ChickenParticle extends TextureSheetParticle {

	/**
	 * Particle identifier.
	 */
	public static final String NAME = ChickenParticle.class.getSimpleName();

	/**
	 * � Sprite set.
	 */
	SpriteSet spriteSet;;

	ChickenParticle(ClientLevel worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	ChickenParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, SpriteSet spriteSet) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.spriteSet = spriteSet;

		/**
		 * // get random Random random = getBassebombeCraft().getRandom();
		 * 
		 * // set speed this.motionX = xSpeedIn * (random.nextDouble() * 2.0D - 1.0D);
		 * this.motionY = ySpeedIn * (random.nextDouble() * 2.0D - 1.0D); this.motionZ =
		 * zSpeedIn * (random.nextDouble() * 2.0D - 1.0D);
		 **/
	}

	@Override
	public void tick() {
		super.tick();

		// animate particle based on age
		if (!removed)
			setSpriteFromAge(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	/**
	 * Particle factory.
	 */
	public static class Factory implements ParticleProvider<SimpleParticleType> {

		/**
		 * Sprite set.
		 */
		SpriteSet spriteSet;

		/**
		 * Constructor
		 * 
		 * @param spriteSet sprite set.
		 */
		public Factory(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel world, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			ChickenParticle particle = new ChickenParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
			particle.setColor(1.0f, 1.0f, 1.0f);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}

	}

}
