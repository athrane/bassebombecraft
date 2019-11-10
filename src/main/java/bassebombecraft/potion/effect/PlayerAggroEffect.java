package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.entity.EntityUtils.setMobEntityAggroed;
import static bassebombecraft.entity.EntityUtils.setTarget;
import static bassebombecraft.entity.EntityUtils.supportTargeting;
import static bassebombecraft.player.PlayerUtils.isPlayerEntityAlive;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import bassebombecraft.config.ModConfiguration;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.world.World;

/**
 * Effect which make a mob aggro's any player, e.g. would attack it on sight.
 */
public class PlayerAggroEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = PlayerAggroEffect.class.getSimpleName();

	/**
	 * Update frequency for effect.
	 */
	int updateFrequency;
	
	/**
	 * Area of effect.
	 */
	int arreaOfEffect;

	/**
	 * PlayerAggroEffect constructor.
	 */
	public PlayerAggroEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		arreaOfEffect = ModConfiguration.playerAggroEffectAreaOfEffect.get();
		updateFrequency  = ModConfiguration.playerAggroEffectUpdateFrequency.get();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// exit if entity isn't a creature to support targeting
		if (!supportTargeting(entity))
			return;

		// get target (either as creature or living entity)
		LivingEntity target = null;
		if (isTypeCreatureEntity(entity)) {
			CreatureEntity entityCreature = (CreatureEntity) entity;
			target = entityCreature.getAttackTarget();
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			target = entityLiving.getLastAttackedEntity();
		}
		
		// exit if target is defined, it is a player and it is alive
		if (isTypePlayerEntity(target) && isPlayerEntityAlive(target))
			return;
		
		// get world
		World world = entity.getEntityWorld();

		// get closet player
		double searchDist = (double) arreaOfEffect;
		Optional<PlayerEntity> optPlayer = ofNullable(world.getClosestPlayer(entity, searchDist));				

		// exit if no targets where found
		if (!optPlayer.isPresent())
			return;
		
		// set target
		setTarget(entity, optPlayer.get());

		// set mob aggro'ed
		setMobEntityAggroed(entity);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int moduloValue = duration % updateFrequency; 
		return (moduloValue == 0);
	}

}
