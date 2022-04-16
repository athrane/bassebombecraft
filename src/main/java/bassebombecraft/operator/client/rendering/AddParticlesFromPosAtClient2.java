package bassebombecraft.operator.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.core.BlockPos;

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
	 * @param info       particle rendering info.
	 * @param fnBlockPos function to get block position where particles should be
	 *                   rendered.
	 */
	public AddParticlesFromPosAtClient2(ParticleRenderingInfo info, Function<Ports, BlockPos> fnBlockPos) {
		this.infos = new ParticleRenderingInfo[] { info };
		this.fnBlockPos = fnBlockPos;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with block position #1 as particle spawn position from
	 * ports.
	 * 
	 * @param info particle rendering info.
	 */
	public AddParticlesFromPosAtClient2(ParticleRenderingInfo info) {
		this(info, getFnGetBlockPosition1());
	}

	@Override
	public void run(Ports ports) {
		BlockPos position = applyV(fnBlockPos,ports);

		// iterate over rendering info's
		for (ParticleRenderingInfo info : infos) {
			// send particle rendering info to client
			ParticleRendering particle = getInstance(position, info);
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
		}
	}
}
