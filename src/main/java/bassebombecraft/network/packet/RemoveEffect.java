package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
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
	public RemoveEffect(FriendlyByteBuf buf) {
		this.effectId = buf.readInt();
		this.entityId = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param entity entity to remove the effect from.
	 * @param effect effect to remove from entity.
	 */
	public RemoveEffect(LivingEntity entity, MobEffect effect) {
		this.entityId = entity.getId();
		this.effectId = MobEffect.getId(effect);
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(FriendlyByteBuf buf) {
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
			Entity entity = mcClient.level.getEntity(entityId);

			// exit if entity isn't defined
			if(entity == null) return;

			// exit if effect isn't defined
			MobEffect effect = MobEffect.byId(effectId);			
			if(effect == null) return;
						
			// remove potion effect
			((LivingEntity) entity).removeEffect(effect);
			
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
