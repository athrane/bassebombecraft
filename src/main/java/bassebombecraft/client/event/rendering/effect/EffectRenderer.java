package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import java.util.stream.Stream;

import bassebombecraft.client.operator.ClientPorts;
import net.minecraftforge.client.event.RenderLevelStageEvent;

/**
 * Client side renderer for rendering graphical effects.
 */
public class EffectRenderer {

	/**
	 * Handle {@linkplain RenderLevelStageEvent} rendering event at client side.
	 * 
	 * The stage {@linkplain RenderLevelStageEvent.Stage#AFTER_PARTICLES} is used
	 * because it is the last stage that fires after all entity and particle
	 * rendering is complete, preserving the original
	 * {@code RenderLevelLastEvent} firing semantics. Note: Forge 40.3.0 does not
	 * provide an {@code AFTER_ENTITIES} stage constant.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderWorldLastEvent(RenderLevelStageEvent event) {
		try {

			// exit if not the correct render stage
			if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
				return;

			// exit if player isn't defined
			if (!isClientSidePlayerDefined())
				return;

			// create port
			ClientPorts ports = getInstance();
			ports.setMatrixStack1(event.getPoseStack());

			// get effects
			GraphicalEffectRepository repository = getProxy().getClientGraphicalEffectRepository();
			Stream<GraphicalEffect> effects = repository.get();

			// loop over effects
			effects.forEach(e -> e.render(ports));

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
