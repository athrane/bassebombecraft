package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.config.ModConfiguration.aggroPlayerEffectAreaOfEffect;
import static bassebombecraft.config.ModConfiguration.aggroPlayerEffectUpdateFrequency;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.entity.EntityUtils.setMobEntityAggroed;
import static bassebombecraft.entity.EntityUtils.setTarget;
import static bassebombecraft.entity.EntityUtils.supportTargeting;
import static bassebombecraft.player.PlayerUtils.isPlayerEntityAlive;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

/**
 * Effect which make the entity with the effect, "aggro" any player , e.g.
 * attack the player on sight.
 * 
 * The effect has no effect on the player.
 */
public class AggroPlayerEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = AggroPlayerEffect.class.getSimpleName();

	/**
	 * PlayerAggroEffect constructor.
	 */
	public AggroPlayerEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

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
			PathfinderMob entityCreature = (PathfinderMob) entity;
			target = entityCreature.getTarget();
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			target = entityLiving.getLastHurtMob();
		}

		// exit if target is defined, it is a player and it is alive
		if (isTypePlayerEntity(target) && isPlayerEntityAlive(target))
			return;

		// get world
		Level world = entity.getCommandSenderWorld();

		// get closet player
		int arreaOfEffect = aggroPlayerEffectAreaOfEffect.get();
		double searchDist = (double) arreaOfEffect;
		Optional<Player> optPlayer = ofNullable(world.getNearestPlayer(entity, searchDist));

		// exit if no targets where found
		if (!optPlayer.isPresent())
			return;

		// set target
		setTarget(entity, optPlayer.get());

		// set mob aggro'ed
		setMobEntityAggroed(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int updateFrequency = aggroPlayerEffectUpdateFrequency.get();
		int moduloValue = duration % updateFrequency;
		return (moduloValue == 0);
	}

}
