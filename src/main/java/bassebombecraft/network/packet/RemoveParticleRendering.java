package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.client.event.rendering.particle.ParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRendering;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Remove particle rendering network packet.
 * 
 * Packet is used to register that particles should stop being rendered. The
 * packet is sent from server to client.
 */
public class RemoveParticleRendering {

	/**
	 * ID used to unregister particle rendering.
	 */
	String id;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public RemoveParticleRendering(PacketBuffer buf) {
		this.id = buf.readString();
	}

	/**
	 * Constructor.
	 * 
	 * @param rendering particle rendering directive.
	 */
	public RemoveParticleRendering(ParticleRendering rendering) {
		this.id = rendering.getId();
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(PacketBuffer buf) {
		buf.writeString(id);
	}

	/**
	 * Handle received network packet.
	 * 
	 * Removes the particle rendering directive from client side.
	 * 
	 * @param buf packet buffer.
	 */
	public void handle(Supplier<NetworkEvent.Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> handlePacket());
		ctx.setPacketHandled(true);
	}

	/**
	 * Handle received network packet.
	 */		
	void handlePacket() {
		try {
			// remove particle rendering directive
			ParticleRenderingRepository repository = getProxy().getClientParticleRenderingRepository();
			repository.remove(id);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
	
}
