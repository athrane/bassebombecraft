package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.stream.Stream;

import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
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
			Ports ports = getInstance();

			// add matrix stack
			ports.setMatrixStack1(event.getMatrixStack());

			// get effects
			GraphicalEffectRepository repository = getProxy().getClientGraphicalEffectRepository();
			Stream<GraphicalEffect> effects = repository.get();

			// loop over effects
			effects.forEach(e -> {
				ports.setEntity1(e.getSource());
				ports.setEntity2(e.getTarget());
				Operators2.run(ports, e.getEffectOperator());
			});

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
