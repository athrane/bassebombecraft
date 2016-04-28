package bassebombecraft.event.block.temporary;

import java.util.List;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Event listener for rendering of temporary blocks.
 */
public class TemporaryBlockEventListener {

	static final int RENDERING_FREQUENCY = 10; // Measured in world ticks
	int ticksCounter = 0;

	/**
	 * Block directives repository.
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Temporary block repository.
	 */
	TemporaryBlockRepository repository;

	/**
	 * ParticleRenderingEventListener constructor.
	 * 
	 * @param directivesRepository
	 *            block directives repository.
	 * @param tempBlockRepository
	 *            temporary block repository.
	 */
	public TemporaryBlockEventListener(TemporaryBlockRepository tempBlockRepository,
			BlockDirectivesRepository directivesRepository) {
		super();
		this.repository = tempBlockRepository;
		this.directivesRepository = directivesRepository;

	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		ticksCounter++;

		// update block duration
		repository.updateBlockDuration();

		// get and render blocks
		List<BlockDirective> blocks = repository.getBlockDirectives();
		directivesRepository.addAll(blocks);
	}

}
