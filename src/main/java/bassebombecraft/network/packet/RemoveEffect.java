package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Remove potion effect network packet.
 * 
 * Packet is used to send ID of potion effect remove from mob. The packet is
 * used to sync the client with the server.
 */
public class RemoveEffect {

	/**
	 * Effect ID.
	 */
	int effectId;

	/**
	 * Entity ID.
	 */
	int entityId;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public RemoveEffect(PacketBuffer buf) {
		this.effectId = buf.readInt();
		this.entityId = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param entity entity to remove the effect from.
	 * @param effect effect to remove from entity.
	 */
	public RemoveEffect(LivingEntity entity, Effect effect) {
		this.entityId = entity.getEntityId();
		this.effectId = Effect.getId(effect);
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(PacketBuffer buf) {
		buf.writeInt(effectId);
		buf.writeInt(entityId);
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
			Entity entity = mcClient.world.getEntityByID(entityId);

			// exit if entity isn't defined
			if(entity == null) return;

			// exit if effect isn't defined
			Effect effect = Effect.get(effectId);			
			if(effect == null) return;
						
			// remove potion effect
			((LivingEntity) entity).removePotionEffect(effect);
			
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
