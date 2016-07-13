package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.player.pvp.PvpRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class initiates player participation in PVP.
 */
public class RegisterForPvp implements InventoryItemActionStrategy {

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.WATER_DROP;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.75F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	/**
	 * PVP repository
	 */
	PvpRepository pvpRepository;

	/**
	 * StartPvp constructor.
	 */
	public RegisterForPvp() {
		super();
		pvpRepository = getBassebombeCraft().getPvpRepository();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return false;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return targetIsInvoker;
	}

	@Override
	public void applyEffect(Entity target, World world) {

		// only apply for players
		if (!(target instanceof EntityPlayer))
			return;

		// type cast
		EntityPlayer player = (EntityPlayer) target;

		// participate in PVP
		pvpRepository.participate(player);
	}

	@Override
	public int getEffectRange() {
		return 1; // Not a AOE effect
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
