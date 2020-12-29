package bassebombecraft.client.rendering;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;

/**
 * Implementation of {@linkplain IRenderTypeBuffer} which returns a buffer with
 * a hard coded {@linkplain RenderType}.
 */
public class RedirectingBuffer implements IRenderTypeBuffer {

	/**
	 * Buffer.
	 */
	final IVertexBuilder vertexBuiler;
	
	/**
	 * Constructor. 
	 * 
	 * @param renderTypeBuffer parent buffer.
	 * @param type render type.
	 */
	public RedirectingBuffer(IRenderTypeBuffer renderTypeBuffer, RenderType type) {
		this.vertexBuiler = renderTypeBuffer.getBuffer(type);
	}	
	
	@Override
	public IVertexBuilder getBuffer(RenderType type) {
		return vertexBuiler;
	}

}
