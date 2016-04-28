package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of mist
 * action. This class builds a mist which charm a mob.
 */
public class BeastmasterMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 1000; // Measured in ticks

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_MOB;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float G = 0.75F;
	static final float B = 0.0F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	@Override
	public void applyEffectToEntity(EntityLivingBase target, Vec3 mistPos) {

		// skip if entity can't be charmed
		if (!(target instanceof EntityLiving))
			return;
		EntityLiving entityLiving = (EntityLiving) target;

		// register mob as charmed
		getBassebombeCraft().getCharmedMobsRepository().add(entityLiving);
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return true;
	}

	@Override
	public boolean isStationary() {
		return true;
	}

	@Override
	public boolean isOneShootEffect() {
		return false;
	}

	@Override
	public int getEffectRange() {
		return 5;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
