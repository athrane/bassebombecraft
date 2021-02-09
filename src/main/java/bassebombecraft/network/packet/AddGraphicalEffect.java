package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;

import java.util.function.Supplier;

import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Add graphical effect network packet.
 * 
 * Packet is used to send ID the two involved entities. The packet is used to
 * sync the client with the server.
 */
public class AddGraphicalEffect {

	/**
	 * Effect duration (in game ticks).
	 */
	int duration;

	/**
	 * Source entity ID.
	 */
	int sourceEntityId;

	/**
	 * Target entity ID.
	 */
	int targetEntityId;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddGraphicalEffect(PacketBuffer buf) {
		this.sourceEntityId = buf.readInt();
		this.targetEntityId = buf.readInt();
		this.duration = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param source   source entity involved in the effect.
	 * @param target   target entity involved in the effect.
	 * @param duration effect duration (in game ticks).
	 */
	public AddGraphicalEffect(LivingEntity source, LivingEntity target, int duration) {
		this.sourceEntityId = source.getEntityId();
		this.targetEntityId = target.getEntityId();
		this.duration = duration;
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(PacketBuffer buf) {
		buf.writeInt(sourceEntityId);
		buf.writeInt(targetEntityId);
		buf.writeInt(duration);
	}

	/**
	 * Handle received network packet.
	 * 
	 * Adds the potion effect to client side entity.
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
			Entity source = mcClient.world.getEntityByID(sourceEntityId);

			// exit if entity isn't defined
			if (source == null)
				return;

			// exit if entity isn't a living entity
			if (!isTypeLivingEntity(source))
				return;

			// type cast
			LivingEntity sourceAsLivingEntity = (LivingEntity) source;

			// get client side entity from ID
			Entity target = mcClient.world.getEntityByID(targetEntityId);

			// exit if entity isn't defined
			if (target == null)
				return;

			// exit if entity isn't a living entity
			if (!isTypeLivingEntity(target))
				return;

			// type cast
			LivingEntity targetAsLivingEntity = (LivingEntity) target;

			// register
			GraphicalEffectRepository repository = getProxy().getClientGraphicalEffectRepository();
			repository.add(sourceAsLivingEntity, targetAsLivingEntity, duration);

			getBassebombeCraft().getLogger().debug("AddGraphicalEffect source=" + source);
			getBassebombeCraft().getLogger().debug("AddGraphicalEffect target=" + target);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
