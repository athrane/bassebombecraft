package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.FriendlyByteBuf;
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
	 * Null Commander value.
	 */
	final static LivingEntity NULL_COMMANDER = null;
	
	/**
	 * Entity ID.
	 */
	int entityId;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddCharm(FriendlyByteBuf buf) {
		this.entityId = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param entity entity which is charmed.
	 */
	public AddCharm(Mob entity) {
		this.entityId = entity.getId();
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(FriendlyByteBuf buf) {
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
			Entity entity = mcClient.level.getEntity(entityId);

			// exit if entity isn't defined
			if(entity == null) return;
			
			// type cast
			Mob mobEntity = (Mob) entity;

			// register
			CharmedMobsRepository repository = getProxy().getClientCharmedMobsRepository();
			repository.add(mobEntity, NULL_COMMANDER);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
