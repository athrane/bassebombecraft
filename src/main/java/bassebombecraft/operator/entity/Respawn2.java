package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AGGRO_PLAYER_EFFECT;
import static bassebombecraft.ModConstants.IS_RESPAWNED;
import static bassebombecraft.config.ModConfiguration.respawnMaxEntities;
import static bassebombecraft.config.ModConfiguration.respawnMinEntities;
import static bassebombecraft.config.ModConfiguration.respawnSpawnArea;
import static bassebombecraft.entity.EntityUtils.calculateRandomYaw;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import bassebombecraft.ModConstants;
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
 */
public class Respawn2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Respawn2.class.getSimpleName();

	/**
	 * Value for {@linkplain ModConstants.IS_RESPAWNED}. The value doesn't carry any
	 * significance
	 */
	static final double IS_RESPAWNED_VALUE = 1.0D;

	/**
	 * Function to get dead entity.
	 */
	Function<Ports, Entity> fnGetDeadEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetDeadEntity function to get dead entity.
	 */
	public Respawn2(Function<Ports, Entity> fnGetDeadEntity) {
		this.fnGetDeadEntity = fnGetDeadEntity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as dead entity from ports.
	 */
	public Respawn2() {
		this(getFnGetEntity1());
	}

	@Override
	public Ports run(Ports ports) {

		// get entitiy
		Entity deadEntity = fnGetDeadEntity.apply(ports);

		if (deadEntity == null)
			return ports;

		// spawn entities
		Random random = getBassebombeCraft().getRandom();
		int entities = Math.max(respawnMinEntities.get(), random.nextInt(respawnMaxEntities.get()));
		IntStream.rangeClosed(1, entities).forEach(n -> spawnEntity(deadEntity));

		return ports;
	}

	/**
	 * Spawn entity, which aggro's the player.
	 * 
	 * @param deadEntity dead entity to spawn from.
	 */
	static void spawnEntity(Entity deadEntity) {

		// get world
		World world = deadEntity.getEntityWorld();

		// get dead entity type
		EntityType<?> type = deadEntity.getType();

		// create new entity
		Entity spawnedEntity = type.create(world);
		int spawnArea = respawnSpawnArea.get();
		setRandomSpawnPosition(deadEntity.getPosition(), calculateRandomYaw(), spawnArea, spawnedEntity);

		// entity is a living entity then set RESPAWNED attribute for rendering
		if (isTypeLivingEntity(spawnedEntity))
			setAttribute((LivingEntity) spawnedEntity, IS_RESPAWNED, IS_RESPAWNED_VALUE);

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
	static EffectInstance createEffect() {
		return new EffectInstance(AGGRO_PLAYER_EFFECT, Integer.MAX_VALUE);
	}

}
