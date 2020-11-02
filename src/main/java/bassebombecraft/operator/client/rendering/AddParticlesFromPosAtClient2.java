package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;

import java.util.function.Function;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Operator2} interface which adds particles
 * at client side.
 * 
 * The particles are added from a block position.
 */
public class AddParticlesFromPosAtClient2 implements Operator2 {

	/**
	 * Rendering infos.
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Function to get block position from ports.
	 */
	Function<Ports, BlockPos> fnBlockPos;

	/**
	 * Constructor.
	 * 
	 * @param particles  particle rendering infos.
	 * @param fnBlockPos function to get block position where particles should be
	 *                   rendered.
	 */
	public AddParticlesFromPosAtClient2(ParticleRenderingInfo[] infos, Function<Ports, BlockPos> fnBlockPos) {
		this.infos = infos;
		this.fnBlockPos = fnBlockPos;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with block position #1 as particle spawn position from
	 * ports.
	 * 
	 * @param particles particle rendering infos.
	 */
	public AddParticlesFromPosAtClient2(ParticleRenderingInfo[] infos) {
		this(infos, getFnGetBlockPosition1());
	}

	@Override
	public Ports run(Ports ports) {

		// get position
		BlockPos pos = fnBlockPos.apply(ports);

		// iterate over rendering info's
		for (ParticleRenderingInfo info : infos) {
			// send particle rendering info to client
			ParticleRendering particle = getInstance(pos, info);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
		}

		return ports;
	}
}
