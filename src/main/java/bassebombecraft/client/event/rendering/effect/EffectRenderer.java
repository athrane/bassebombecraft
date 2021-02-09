package bassebombecraft.client.event.rendering.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.stream.Stream;

import bassebombecraft.client.op.rendering.RenderLine2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Client side renderer for rendering graphical effects.
 */
public class EffectRenderer {

	/**
	 * Effect operator
	 */
	static Operator2 operator = new RenderLine2();

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
			ports.setMatrixStack(event.getMatrixStack());

			// get effects
			GraphicalEffectRepository repository = getProxy().getClientGraphicalEffectRepository();
			Stream<GraphicalEffect> effects = repository.get();

			// loop over effects
			effects.forEach(e -> renderEffect(e, ports));

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render effect. 
	 * 
	 * @param effect
	 * @param ports
	 */
	static void renderEffect(GraphicalEffect effect, Ports ports) {
		LivingEntity source = effect.getSource();
		LivingEntity target= effect.getTarget();		
		Vec3d[] vectors = { source.getPositionVec(), target.getPositionVec() };
		ports.setVectors1(vectors);
		Operators2.run(ports, operator);
	}

}
