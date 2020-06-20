package bassebombecraft.event.block.temporary;

import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.List;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for rendering of temporary blocks.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class TemporaryBlockEventHandler {

	@SubscribeEvent
	public static void handleServerTickEvent(ServerTickEvent event) {

		// get repositories
		BlockDirectivesRepository directivesRepository = getProxy().getServerBlockDirectivesRepository();
		TemporaryBlockRepository repository = getProxy().getServerTemporaryBlockRepository();

		// update block duration
		repository.updateBlockDuration();

		// get and render blocks
		List<BlockDirective> blocks = repository.getBlockDirectives();
		directivesRepository.addAll(blocks);
	}

}
