package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.operator.DefaultClientPorts.getInstance;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import java.util.stream.Stream;

import bassebombecraft.client.operator.ClientPorts;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Client side renderer for rendering graphical effects.
 */
public class EffectRenderer {

	/**
	 * Handle {@linkplain RenderWorldLastEvent} rendering event at client side.
	 * 
	 * @param event rendering event.
	 */
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player isn't defined
			if (!isClientSidePlayerDefined())
				return;

			// create port
			ClientPorts ports = getInstance();
			ports.setMatrixStack1(event.getMatrixStack());

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
