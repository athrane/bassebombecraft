package bassebombecraft.client.operator;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import bassebombecraft.operator.DefaultPorts;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Implementation of {@linkplain ClientPorts}.
 */
public class DefaultClientPorts extends DefaultPorts implements ClientPorts {

	/**
	 * Matrix stack.
	 */
	PoseStack matrixStack;

	/**
	 * RenderGameOverlayEvent.
	 */
	RenderGameOverlayEvent renderGameOverlayEvent;
	
	/**
	 * Matrix stack #1 getter.
	 */
	static Function<ClientPorts, PoseStack> fnGetMatrixStack1 = p -> p.getMatrixStack1();

	/**
	 * Matrix stack #1 setter.
	 */
	static BiConsumer<ClientPorts, PoseStack> bcSetMatrixStack1 = (p, ms) -> p.setMatrixStack1(ms);

	/**
	 * RenderGameOverlayEvent #1 getter.
	 */
	static Function<ClientPorts, RenderGameOverlayEvent> fnGetRenderGameOverlayEvent1 = p -> p.getRenderGameOverlayEvent1();

	/**
	 * RenderGameOverlayEvent #1 setter.
	 */
	static BiConsumer<ClientPorts, RenderGameOverlayEvent> bcSetRenderGameOverlayEvent1 = (p, e) -> p.setRenderGameOverlayEvent1(e);
	
	/**
	 * Constructor
	 */
	DefaultClientPorts() {
		super();
	}

	@Override
	public PoseStack getMatrixStack1() {
		return matrixStack;
	}

	@Override
	public ClientPorts setMatrixStack1(PoseStack ms) {
		this.matrixStack = ms;
		return this;
	}
	
	@Override
	public RenderGameOverlayEvent getRenderGameOverlayEvent1() {
		return renderGameOverlayEvent;
	}

	@Override
	public ClientPorts setRenderGameOverlayEvent1(RenderGameOverlayEvent event) {
		this.renderGameOverlayEvent = event;
		return this;
	}

	public static Function<ClientPorts, PoseStack> getFnMaxtrixStack1() {
		return fnGetMatrixStack1;
	}

	public static BiConsumer<ClientPorts, PoseStack> getBcSetMaxtrixStack1() {
		return bcSetMatrixStack1;
	}

	public static Function<ClientPorts, RenderGameOverlayEvent> getFnRenderGameOverlayEvent1() {
		return fnGetRenderGameOverlayEvent1;
	}

	public static BiConsumer<ClientPorts, RenderGameOverlayEvent> getBcSetRenderGameOverlayEvent1() {
		return bcSetRenderGameOverlayEvent1;
	}
	
	/**
	 * Factory method.
	 * 
	 * @return ports.
	 */
	public static ClientPorts getInstance() {
		return new DefaultClientPorts();
	}

}
