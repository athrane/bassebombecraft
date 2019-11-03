package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.MOB_RESPAWNING_EFFECT;
import static bassebombecraft.ModConstants.PLAYER_AGGRO_EFFECT;
import static bassebombecraft.entity.EntityUtils.calculateRandomYaw;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.stream.IntStream;

import bassebombecraft.config.ModConfiguration;
import static bassebombecraft.potion.PotionUtils.*;
import bassebombecraft.potion.effect.MobRespawningEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the mob respawning potion effect.
 * 
 * Logic for the {@linkplain MobRespawningEffect}.
 */
@Mod.EventBusSubscriber
public class MobRespawningEffectEventHandler {

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {

		// exit if player isn't defined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		// exit if effect isn't active
		Optional<EffectInstance> optEffect = getEffectIfActive(player, MOB_RESPAWNING_EFFECT);
		if (!optEffect.isPresent())
			return;

		// get amplifier from active effect
		int amplifier = optEffect.get().getAmplifier();

		// handle event if living entity is defined in event
		Optional<LivingEntity> optLivingEntity = ofNullable(event.getEntityLiving());
		if (!optLivingEntity.isPresent())
			return;

		// get dead entity
		Entity deadEntity = optLivingEntity.get();

		// exit if dead entity is outside of area-of-effect of the player
		float distance = player.getDistance(deadEntity);
		if (distance > ModConfiguration.mobRespawningAreaOfEffect.get())
			return;

		// spawn entities
		int spawnArea = ModConfiguration.mobRespawningEffectDuration.get();
		int entities = calculateEntities(player, amplifier);

		IntStream.rangeClosed(0, entities).forEach(n -> spawnEntity(deadEntity, player, spawnArea));
	}

	/**
	 * Calculate number of entities to spawn.
	 * 
	 * @param player    player entity
	 * @param amplifier current potion amplifier.
	 * 
	 * @return number of entities to spawn.
	 */
	static int calculateEntities(PlayerEntity player, int amplifier) {

		// use existing amplifier if amplifier effect isn't active
		if (!isAmplifierEffectActive(player)) {

			// spawn at least one entity
			return Math.max(1, amplifier);
		}

		// if amplifier in active amplifier effect is 16,64,128 then result should be
		// 2+1,8,16
		int entities = (amplifier / 8) + 1;

		// spawn at least one entity
		return Math.max(1, entities);
	}

	/**
	 * Spawn entity, which aggro's the player.
	 * 
	 * @param deadEntity dead entity to spawn from.
	 * @param player     player to aggro.
	 * @param spawnArea  spawn areas size.
	 */
	static void spawnEntity(Entity deadEntity, PlayerEntity player, int spawnArea) {

		// get world
		World world = deadEntity.getEntityWorld();

		// get dead entity type
		EntityType<?> type = deadEntity.getType();

		// create new entity
		Entity spawnedEntity = type.create(world);
		setRandomSpawnPosition(deadEntity.getPosition(), calculateRandomYaw(), spawnArea, spawnedEntity);
		world.addEntity(spawnedEntity);

		// clone equipment

		// apply player player aggro potion
		LivingEntity livingEntity = (LivingEntity) spawnedEntity;
		livingEntity.addPotionEffect(createEffect());
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	static EffectInstance createEffect() {
		return new EffectInstance(PLAYER_AGGRO_EFFECT, ModConfiguration.mobRespawningEffectDuration.get());
	}

}
