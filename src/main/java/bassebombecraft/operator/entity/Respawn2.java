package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MARKER_ATTRIBUTE_IS_SET;
import static bassebombecraft.config.ModConfiguration.respawnMaxEntities;
import static bassebombecraft.config.ModConfiguration.respawnMinEntities;
import static bassebombecraft.config.ModConfiguration.respawnSpawnArea;
import static bassebombecraft.entity.EntityUtils.calculateRandomYaw;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.entity.attribute.RegisteredAttributes.IS_RESPAWNED_ATTRIBUTE;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.potion.effect.RegisteredEffects.AGGRO_PLAYER_EFFECT;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which respawns any
 * number of instances of an (dead) entity .
 * 
 * Spawned entities aggros the player.
 */
public class Respawn2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Respawn2.class.getSimpleName();

	/**
	 * Function to get dead entity.
	 */
	Function<Ports, LivingEntity> fnGetDeadEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetDeadEntity function to get dead living entity.
	 */
	public Respawn2(Function<Ports, LivingEntity> fnGetDeadEntity) {
		this.fnGetDeadEntity = fnGetDeadEntity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as dead entity from ports.
	 */
	public Respawn2() {
		this(getFnGetLivingEntity1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity deadEntity = applyV(fnGetDeadEntity, ports);

		// spawn entities
		Random random = getBassebombeCraft().getRandom();
		int entities = Math.max(respawnMinEntities.get(), random.nextInt(respawnMaxEntities.get()));
		IntStream.rangeClosed(1, entities).forEach(n -> spawnEntity(deadEntity));
	}

	/**
	 * Spawn entity, which aggro's the player.
	 * 
	 * @param deadEntity dead entity to spawn from.
	 */
	void spawnEntity(LivingEntity deadEntity) {

		// get world
		World world = deadEntity.getEntityWorld();

		// get dead entity type
		EntityType<?> type = deadEntity.getType();

		// create new entity
		Entity spawnedEntity = type.create(world);
		int spawnArea = respawnSpawnArea.get();
		setRandomSpawnPosition(deadEntity.getPosition(), calculateRandomYaw(), spawnArea, spawnedEntity);

		// entity is a living entity then set IS_RESPAWNED attribute for rendering
		if (isTypeLivingEntity(spawnedEntity))
			setAttribute((LivingEntity) spawnedEntity, IS_RESPAWNED_ATTRIBUTE.get(), MARKER_ATTRIBUTE_IS_SET);

		// spawn
		world.addEntity(spawnedEntity);

		// clone equipment

		// apply player aggro potion
		LivingEntity livingEntity = (LivingEntity) spawnedEntity;
		livingEntity.addPotionEffect(createEffect());
	}

	/**
	 * Create player aggro potion effect with indefinite duration.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(AGGRO_PLAYER_EFFECT.get(), Integer.MAX_VALUE);
	}

}
