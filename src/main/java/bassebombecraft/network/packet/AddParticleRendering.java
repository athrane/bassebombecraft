package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import java.util.function.Supplier;

import bassebombecraft.client.event.rendering.particle.ParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Add particle rendering network packet.
 * 
 * Packet is used to register that particles should rendered for some duration.
 * The packet is sent from server to client.
 */
public class AddParticleRendering {

	/**
	 * ID used to register particle rendering.
	 */
	String id;

	/**
	 * Position.
	 */
	BlockPos position;

	/**
	 * Rendering duration.
	 */
	int duration;

	/**
	 * Particle speed
	 */
	double speed;

	/**
	 * Number particles per update.
	 */
	int number;

	/**
	 * RGB red.
	 */
	float rgbRed;

	/**
	 * RGB green.
	 */
	float rgbGreen;

	/**
	 * RGB blue.
	 */
	float rgbBlue;

	/**
	 * Particle type.
	 */
	SimpleParticleType type;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddParticleRendering(FriendlyByteBuf buf) {
		this.id = buf.readUtf();
		this.position = buf.readBlockPos();
		this.duration = buf.readInt();
		this.speed = buf.readDouble();
		this.number = buf.readInt();
		this.rgbRed = buf.readFloat();
		this.rgbGreen = buf.readFloat();
		this.rgbBlue = buf.readFloat();
		this.type = (SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(buf.readResourceLocation());
	}

	/**
	 * Constructor.
	 * 
	 * @param rendering particle rendering directive.
	 */
	public AddParticleRendering(ParticleRendering rendering) {
		this.id = rendering.getId();
		this.position = rendering.getPosition();
		this.duration = rendering.getInfo().getDuration();
		this.speed = rendering.getInfo().getSpeed();
		this.number = rendering.getNumber();
		this.rgbRed = rendering.getInfo().getRedColorComponent();
		this.rgbGreen = rendering.getInfo().getGreenColorComponent();
		this.rgbBlue = rendering.getInfo().getBlueColorComponent();
		this.type = rendering.getInfo().getParticleType();
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(id);
		buf.writeBlockPos(position);
		buf.writeInt(duration);
		buf.writeDouble(speed);
		buf.writeInt(number);
		buf.writeFloat(rgbRed);
		buf.writeFloat(rgbGreen);
		buf.writeFloat(rgbBlue);
		buf.writeResourceLocation(type.getRegistryName());
	}

	/**
	 * Handle received network packet.
	 * 
	 * Create and adds the particle rendering directive to client side.
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
			// create particle info
			ParticleRenderingInfo info = getInstance(type, number, duration, rgbRed, rgbGreen, rgbBlue, speed);

			// create particle rendering
			ParticleRendering particle = getInstance(position, info);

			// register for rendering
			ParticleRenderingRepository repository = getProxy().getClientParticleRenderingRepository();
			repository.add(id, particle);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
