package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;

import java.util.function.Function;

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
 * The particles are added from the position of the living entity.
 */
public class AddParticlesFromEntityAtClient2 implements Operator2 {

	/**
	 * Rendering infos.
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetLivingEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get living entity.
	 * @param particles   particle rendering infos.
	 */
	public AddParticlesFromEntityAtClient2(Function<Ports, LivingEntity> fnGetLivingEntity,
			ParticleRenderingInfo[] infos) {
		this.fnGetLivingEntity = fnGetLivingEntity;
		this.infos = infos;
	}

	/**
	 * Constructor.
	 * 
	 * @param particles particle rendering infos.
	 * 
	 *                  Instance is configured with living entity #1 (at whose
	 *                  position the particle are spawned) source from ports.
	 */
	public AddParticlesFromEntityAtClient2(ParticleRenderingInfo[] infos) {
		this(getFnGetLivingEntity1(), infos);
	}

	@Override
	public Ports run(Ports ports) {

		// get entity
		LivingEntity entity = fnGetLivingEntity.apply(ports);
		if (entity == null)
			return ports;

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
