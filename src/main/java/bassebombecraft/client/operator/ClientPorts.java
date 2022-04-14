package bassebombecraft.client.operator;

import com.mojang.blaze3d.matrix.MatrixStack;

import bassebombecraft.operator.Ports;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Client side collection of ports which provides input and output from operators.
 */
public interface ClientPorts extends Ports {

	/**
	 * Get matrix stack #1.
	 * 
	 * @return matrix stack.
	 */
	MatrixStack getMatrixStack1();

	/**
	 * Set matrix stack #1.
	 * 
	 * @param ms matrix stack.
	 * 
	 * @return ports.
	 */
	ClientPorts setMatrixStack1(MatrixStack ms);

	/**
	 * Get {@linkplain RenderGameOverlayEvent} #1.
	 * 
	 * @return {@linkplain RenderGameOverlayEvent}.
	 */
	RenderGameOverlayEvent getRenderGameOverlayEvent1();

	/**
	 * Set {@linkplain RenderGameOverlayEvent} #1.
	 * 
	 * @param event{@linkplain RenderGameOverlayEvent}.
	 * 
	 * @return ports.
	 */
	ClientPorts setRenderGameOverlayEvent1(RenderGameOverlayEvent event);
		
}
