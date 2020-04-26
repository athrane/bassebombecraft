package bassebombecraft.event.entity.target;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.world.WorldUtils.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for handling targeted entities.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class TargetedEntitiesEventHandler {

	@SubscribeEvent
	static public void handleLivingDeathEvent(LivingDeathEvent event) {

		// exit if handler is executed at client side
		if (isLogicalClient(event.getEntity()))
			return;

		// get repository
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();

		// remove entity from team upon death
		LivingEntity entity = event.getEntityLiving();
		repository.remove(entity);

		// delete commanders targets if dead entity is commander
		if (isTypePlayerEntity(event.getEntityLiving())) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			// exit if player isn't a commander
			if (!repository.isCommander(player))
				return;

			// clear all targets
			repository.clear(player);
		}

	}

	@SubscribeEvent
	static public void handleAttackEntityEvent(AttackEntityEvent event) {

		// exit if handler is executed at client side
		if (isLogicalClient(event.getEntity()))
			return;

		// get player and target
		PlayerEntity player = event.getPlayer();
		Entity target = event.getTarget();

		// exit if target isn't a living entity
		if (!isTypeLivingEntity(target))
			return;

		// type cast
		LivingEntity targetAsLivingEntity = (LivingEntity) target;

		// add target for commander
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		repository.add(player, targetAsLivingEntity);
	}

	@SubscribeEvent
	static public void handlePlayerLoggedInEvent(PlayerLoggedInEvent event) {

		// exit if handler is executed at client side
		if (isLogicalClient(event.getEntity()))
			return;

		// register targets for commander
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		repository.createTargets(event.getPlayer());
	}

	@SubscribeEvent
	static public void handlePlayerLoggedOutEvent(PlayerLoggedOutEvent event) {

		// exit if handler is executed at client side
		if (isLogicalClient(event.getEntity()))
			return;

		// delete targets for commander
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
		repository.deleteTargets(event.getPlayer());
	}

}
