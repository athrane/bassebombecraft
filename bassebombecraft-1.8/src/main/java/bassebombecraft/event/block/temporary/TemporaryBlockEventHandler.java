package bassebombecraft.event.block.temporary;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.List;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Event handler for rendering of temporary blocks.
 */
@Mod.EventBusSubscriber
public class TemporaryBlockEventHandler {

	@SubscribeEvent
	public static void handleEvent(PlayerTickEvent event) {		
		
		// get repositories		
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		TemporaryBlockRepository repository = getBassebombeCraft().getTemporaryBlockRepository();
		
		// update block duration
		repository.updateBlockDuration();

		// get and render blocks
		List<BlockDirective> blocks = repository.getBlockDirectives();
		directivesRepository.addAll(blocks);
	}

}