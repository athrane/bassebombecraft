package bassebombecraft.network;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.fml.network.NetworkRegistry.newSimpleChannel;

import bassebombecraft.network.packet.AddEffect;
import bassebombecraft.network.packet.RemoveEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Helper class for managing {@linkplain SimpleChannel}.
 * 
 * Inspirational links:
 * https://mcforge.readthedocs.io/en/latest/networking/simpleimpl/
 * https://wiki.mcjty.eu/modding/index.php?title=Tut14_Ep10
 * https://www.minecraftforge.net/forum/topic/71901-1142-solved-how-do-i-send-packets-to-the-client/
 */
public class NetworkChannelHelper {

	/**
	 * Network channel name.
	 */
	static final ResourceLocation CHANNEL_NAME = new ResourceLocation(MODID, "main");

	/**
	 * Network protocol version.
	 */
	static final String PROTOCOL_VERSION = "1";

	/**
	 * Network channel.
	 */
	SimpleChannel channel;

	/**
	 * No-arg constructor.
	 */
	public NetworkChannelHelper() {
		channel = newSimpleChannel(CHANNEL_NAME, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
				PROTOCOL_VERSION::equals);

		// define index for message registration
		int msgIndex = 0;

		// register messages
		channel.registerMessage(msgIndex++, AddEffect.class, AddEffect::encode, AddEffect::new, AddEffect::handle);
		channel.registerMessage(msgIndex++, RemoveEffect.class, RemoveEffect::encode, RemoveEffect::new, RemoveEffect::handle);		
	}

	/**
	 * Send {@linkplain AddEffect} network packet from server to client.
	 * 
	 * @param entity entity to add the effect to.
	 * @param effect effect to add to entity.
	 */
	public void sendAddEffectPacket(LivingEntity entity, EffectInstance effectInstance) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new AddEffect(entity, effectInstance));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Send {@linkplain RemoveEffect} network packet from server to client.
	 * 
	 * @param entity entity to remove effect from.
	 * @param effect effect to remove from entity.
	 */
	public void sendRemoveEffectPacket(LivingEntity entity, Effect effect) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new RemoveEffect(entity, effect));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
	
}
