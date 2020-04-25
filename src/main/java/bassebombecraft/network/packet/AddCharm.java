package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ClientSideCharmedMobsRepository;
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
	 * {@linkplain ClientSideCharmedMobsRepository}..
	 * 
	 * @param buf packet buffer.
	 */
	public void handle(Supplier<NetworkEvent.Context> context) {
		Context ctx = context.get();

		try {
			ctx.enqueueWork(() -> {

				// get client side entity from ID
				Minecraft mcClient = Minecraft.getInstance();
				Entity entity = mcClient.world.getEntityByID(entityId);

				// type cast
				MobEntity mobEntity = (MobEntity) entity;

				// register
				CharmedMobsRepository repository = BassebombeCraft.getProxy().getCharmedMobsRepository();
				repository.add(mobEntity, null);
			});

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

		ctx.setPacketHandled(true);
	}
}
