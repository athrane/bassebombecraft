package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of mist
 * action. This class creates a lingering flame which explodes when a mob comes
 * near.
 */
public class LingeringFlameMist implements EntityMistActionStrategy {
	
	int explosionRadius;
	
	/**
	 * Particle rendering info.
	 */
	ParticleRenderingInfo info;

	/**
	 * LingeringFlameMist constructor.
	 * 
	 * @param explosionRadius explosion radius in blocks.
	 */
	public LingeringFlameMist(int explosionRadius) {
		this.explosionRadius = explosionRadius;
		
		// initiate particle rendering info 
		float r = 0.75F;
		float g = 0.25F;
		float b = 0.25F;		
		int numbers = 4;
		BasicParticleType type = ParticleTypes.FLAME;
		int duration = 20;		
		double speed = 0.075;
		info = getInstance(type, numbers, duration, r, g, b, speed);		
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3d mistPos, LivingEntity invoker) {
		World world = target.getEntityWorld();
		world.createExplosion(target, mistPos.x, mistPos.y, mistPos.z, explosionRadius, Explosion.Mode.DESTROY);
	}

	@Override
	public int getEffectDuration() {
		return 600;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return false;
	}

	@Override
	public boolean isStationary() {
		return true;
	}

	@Override
	public boolean isOneShootEffect() {
		return true;
	}

	@Override
	public int getEffectRange() {
		return 2;
	}
	
	@Override
	public ParticleRenderingInfo getRenderingInfos() {
		return info;
	}
	
	
}
