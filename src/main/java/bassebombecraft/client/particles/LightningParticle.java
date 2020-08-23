package bassebombecraft.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Lightning particle.
 */
@OnlyIn(Dist.CLIENT)
public class LightningParticle extends SpriteTexturedParticle {

	/**
	 * Particle identifier.
	 */
	public static final String NAME = LightningParticle.class.getSimpleName();

	/** �
	 * Sprite set.
	 */
	IAnimatedSprite spriteSet;;
	
	protected LightningParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	protected LightningParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, IAnimatedSprite spriteSet) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.spriteSet = spriteSet;
	}

	
	@Override
	public void tick() {
		super.tick();

		// animate particle based on age
		if(!isExpired)
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
			LightningParticle particle = new LightningParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
			particle.setColor(1.0f, 1.0f, 1.0f);
			particle.selectSpriteWithAge(spriteSet);			
			//selectSpriteRandomly(spriteSet);
			return particle;
		}

	}

}
