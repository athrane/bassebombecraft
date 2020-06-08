package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Operator2} interface which adds particles
 * at client side.
 * 
 * The particles are added at the entity position.
 */
public class AddParticlesAtClient2 implements Operator2 {

	/**
	 * Rendering infos.
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Constructor.
	 * 
	 * @param particles particle rendering infos.
	 */
	public AddParticlesAtClient2(ParticleRenderingInfo[] infos) {
		this.infos = infos;
	}

	@Override
	public Ports run(Ports ports) {

		// get entity
		LivingEntity entity = ports.getLivingEntity();

		// get entity position
		BlockPos pos = entity.getPosition();

		// iterate over rendering info's
		for (ParticleRenderingInfo info : infos) {
			// send particle rendering info to client
			ParticleRendering particle = getInstance(pos, info);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
		}

		return ports;
	}
}
