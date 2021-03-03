package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Supplier;

import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository;
import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
	 * Graphical effect.
	 */
	Effect effect;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddGraphicalEffect(PacketBuffer buf) {
		this.sourceEntityId = buf.readInt();
		this.targetEntityId = buf.readInt();
		this.duration = buf.readInt();
		this.effect = Effect.valueOf(buf.readString());
	}

	/**
	 * Constructor.
	 * 
	 * @param source   source entity involved in the effect.
	 * @param target   target entity involved in the effect.
	 * @param duration effect duration (in game ticks).
	 * @param effect   graphical effect.
	 */
	public AddGraphicalEffect(Entity source, Entity target, int duration, Effect effect) {
		this.sourceEntityId = source.getEntityId();
		this.targetEntityId = target.getEntityId();
		this.duration = duration;
		this.effect = effect;
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
		buf.writeString(effect.name());
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

			// get client side entity from ID
			Entity target = mcClient.world.getEntityByID(targetEntityId);

			// exit if entity isn't defined
			if (target == null)
				return;

			// register
			GraphicalEffectRepository repository = getProxy().getClientGraphicalEffectRepository();
			repository.add(source, target, duration, effect);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
