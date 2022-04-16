package bassebombecraft.network.packet;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Add potion effect network packet.
 * 
 * Packet is used to send ID of potion effect added to mob. The packet is used
 * to sync the client with the server.
 */
public class AddPotionEffect {

	/**
	 * Effect ID.
	 */
	int effectId;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * Entity ID.
	 */
	int entityId;

	/**
	 * Constructor.
	 * 
	 * @param buf packet buffer.
	 */
	public AddPotionEffect(FriendlyByteBuf buf) {
		this.effectId = buf.readInt();
		this.entityId = buf.readInt();
		this.duration = buf.readInt();
		this.amplifier = buf.readInt();
	}

	/**
	 * Constructor.
	 * 
	 * @param entity entity to add the effect to.
	 * @param effect effect to add to entity.
	 */
	public AddPotionEffect(LivingEntity entity, MobEffectInstance effectInstance) {
		this.entityId = entity.getId();
		this.effectId = MobEffect.getId(effectInstance.getEffect());
		this.duration = effectInstance.getDuration();
		this.amplifier = effectInstance.getAmplifier();
	}

	/**
	 * Encode packet into buffer.
	 * 
	 * @param buf packet buffer.
	 */
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(effectId);
		buf.writeInt(entityId);
		buf.writeInt(duration);
		buf.writeInt(amplifier);
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

			// add potion effect			
			MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
			((LivingEntity) entity).addEffect(effectInstance);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}
}
