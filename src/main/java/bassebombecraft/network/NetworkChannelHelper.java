package bassebombecraft.network;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.fml.network.NetworkRegistry.newSimpleChannel;

import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.network.packet.AddCharm;
import bassebombecraft.network.packet.AddGraphicalEffect;
import bassebombecraft.network.packet.AddParticleRendering;
import bassebombecraft.network.packet.AddPotionEffect;
import bassebombecraft.network.packet.RemoveEffect;
import bassebombecraft.network.packet.RemoveParticleRendering;
import bassebombecraft.proxy.Proxy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Helper class for managing {@linkplain SimpleChannel}.
 * 
 * The helper is used at SERVER side. Access to the repository is supported via
 * sided proxy, i.e.{@linkplain Proxy}.
 * 
 * Links: https://mcforge.readthedocs.io/en/latest/networking/simpleimpl/
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
	static final String NETWORK_PROTOCOL_VERSION = "1";

	/**
	 * Network channel.
	 */
	SimpleChannel channel;

	/**
	 * No-arg constructor.
	 */
	public NetworkChannelHelper() {
		channel = newSimpleChannel(CHANNEL_NAME, () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals,
				NETWORK_PROTOCOL_VERSION::equals);

		// define index for message registration
		int msgIndex = 0;

		// register messages
		channel.registerMessage(msgIndex++, AddPotionEffect.class, AddPotionEffect::encode, AddPotionEffect::new,
				AddPotionEffect::handle);
		channel.registerMessage(msgIndex++, RemoveEffect.class, RemoveEffect::encode, RemoveEffect::new,
				RemoveEffect::handle);
		channel.registerMessage(msgIndex++, AddParticleRendering.class, AddParticleRendering::encode,
				AddParticleRendering::new, AddParticleRendering::handle);
		channel.registerMessage(msgIndex++, RemoveParticleRendering.class, RemoveParticleRendering::encode,
				RemoveParticleRendering::new, RemoveParticleRendering::handle);
		channel.registerMessage(msgIndex++, AddCharm.class, AddCharm::encode, AddCharm::new, AddCharm::handle);
		channel.registerMessage(msgIndex++, AddGraphicalEffect.class, AddGraphicalEffect::encode,
				AddGraphicalEffect::new, AddGraphicalEffect::handle);
	}

	/**
	 * Send {@linkplain AddPotionEffect} network packet from server to client.
	 * 
	 * @param entity entity to add the effect to.
	 * @param effect effect to add to entity.
	 */
	public void sendAddPotionEffectPacket(LivingEntity entity, MobEffectInstance effectInstance) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new AddPotionEffect(entity, effectInstance));
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
	public void sendRemoveEffectPacket(LivingEntity entity, MobEffect effect) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new RemoveEffect(entity, effect));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Send {@linkplain AddParticleRendering} network packet from server to client.
	 * 
	 * @param rendering particle rendering directive.
	 */
	public void sendAddParticleRenderingPacket(ParticleRendering rendering) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new AddParticleRendering(rendering));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Send {@linkplain RemoveParticleRendering} network packet from server to
	 * client.
	 * 
	 * @param rendering particle rendering directive.
	 */
	public void sendRemoveParticleRenderingPacket(ParticleRendering rendering) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new RemoveParticleRendering(rendering));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Send {@linkplain AddCharm} network packet from server to client.
	 * 
	 * Charm is removed at client side by duration repository.
	 * 
	 * @param entity charmed mob.
	 */
	public void sendAddCharmPacket(Mob entity) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new AddCharm(entity));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Send {@linkplain AddGraphicalEffect} network packet from server to client.
	 * 
	 * @param source   source entity involved in the effect.
	 * @param target   target entity involved in the effect.
	 * @param duration effect duration (in game ticks).
	 * @param effect   graphical effect .
	 */
	public void sendAddGraphicalEffectPacket(Entity source, Entity target, int duration,
			GraphicalEffectRepository.Effect effect) {
		try {
			channel.send(PacketDistributor.ALL.noArg(), new AddGraphicalEffect(source, target, duration, effect));
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
