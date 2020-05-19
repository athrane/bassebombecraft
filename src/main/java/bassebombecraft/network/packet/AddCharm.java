package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Add charm effect network packet.
 * 
 * Packet is used to send ID of charmed mob. The packet is used to sync the
 * client with the server.
 */
public class AddCharm {

	/**
	 * Entity ID.
	 */
	int entityId;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddCharm(PacketBuffer buf) {
		this.entityId = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param entity entity which is charmed.
	 */
	public AddCharm(MobEntity entity) {
		this.entityId = entity.getEntityId();
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(PacketBuffer buf) {
		buf.writeInt(entityId);
	}

	/**
	 * Handle received network packet.
	 * 
	 * Registers the mob as charmed at the client side implementation of
	 * {@linkplain CharmedMobsRepository} which is
	 * {@linkplain ClientCharmedMobsRepository}..
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

			// get client side entity from ID
			Minecraft mcClient = Minecraft.getInstance();
			Entity entity = mcClient.world.getEntityByID(entityId);

			// exit if entity isn't defined
			if(entity == null) return;
			
			// type cast
			MobEntity mobEntity = (MobEntity) entity;

			// register
			CharmedMobsRepository repository = getProxy().getClientCharmedMobsRepository();
			repository.add(mobEntity, null);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
