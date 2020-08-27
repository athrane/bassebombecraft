package bassebombecraft.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;

/**
 * Circle particle.
 * 
 * Supports animation based on age.
 * 
 * The set of animation frame is defined in the the .json file.
 */
public class CircleParticle extends SpriteTexturedParticle {

	/**
	 * Particle identifier.
	 */
	public static final String NAME = CircleParticle.class.getSimpleName();

	/**
	 * � Sprite set.
	 */
	IAnimatedSprite spriteSet;;

	protected CircleParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	protected CircleParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, IAnimatedSprite spriteSet) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.spriteSet = spriteSet;
		
		// set speed
		this.motionX = xSpeedIn * (Math.random() * 2.0D - 1.0D);
	    this.motionY = ySpeedIn * (Math.random() * 2.0D - 1.0D);
	    this.motionZ = zSpeedIn * (Math.random() * 2.0D - 1.0D);		
	}

	@Override
	public void tick() {
		super.tick();

		// animate particle based on age
		if (!isExpired)
			selectSpriteWithAge(spriteSet);
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	/**
	 * Particle factory.
	 */
	public static class Factory implements IParticleFactory<BasicParticleType> {

		/**
		 * Sprite set.
		 */
		IAnimatedSprite spriteSet;

		/**
		 * Constructor
		 * 
		 * @param spriteSet sprite set.
		 */
		public Factory(IAnimatedSprite spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, World world, double x, double y, double z, double xSpeed,
				double ySpeed, double zSpeed) {
			CircleParticle particle = new CircleParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
			particle.setColor(1.0f, 1.0f, 1.0f);
			particle.selectSpriteWithAge(spriteSet);
			return particle;
		}

	}

}
